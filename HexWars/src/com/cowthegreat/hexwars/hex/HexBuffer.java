package com.cowthegreat.hexwars.hex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class HexBuffer {
	
	public static class HexDescriptor{
		public HexDescriptor(){
			inner = Color.BLACK;
			outer = Color.RED;
		}
		
		public HexDescriptor(Color inner, Color outer){
			this.inner = inner;
			this.outer = outer;
		}
		
		public Color inner;
		public Color outer;
	}

	public final int hexCount;
	public final int vertexCount;
	public final int indexCount;

	private static final int VERTS_PER_HEX = 6 + 1; // 6 edges & a center
	private static final int IND_PER_HEX = 6 * 3; // 6 triangles with 3 vertices
													// each
	private static final int VERTEX_LENGTH = 3 + 4 + 3; // 3 for position, and 4
														// for color, and 3 for
														// marking the center

	private ShaderProgram shader;
	private Mesh mesh;

	float[] bufferVerts;
	short[] bufferIndex;

	int count;
	int index;

	private boolean hasBegun;

	private Color outlineColor = new Color(1, 1, 1, 1);
	private float glowStrength = 0.25f;
	private float outerRadius = 0.95f;
	private float innerRadius = 0.25f;

	public HexBuffer(int size) {
		hexCount = size;

		vertexCount = VERTS_PER_HEX * hexCount;
		indexCount = IND_PER_HEX * hexCount;

		mesh = new Mesh(false, vertexCount, indexCount, new VertexAttribute(
				Usage.Position, 3, "a_position"), new VertexAttribute(
				Usage.Color, 4, "a_color"), new VertexAttribute(Usage.Generic,
				3, "a_center"));

		FileHandle vshHandle = Gdx.files.internal("shaders/color_shader.vsh");
		FileHandle fshHandle = Gdx.files.internal("shaders/color_shader.fsh");
		
		System.out.println(vshHandle.exists() + " " + fshHandle.exists());
		
		try{
			shader = new ShaderProgram(vshHandle, fshHandle);
		} catch(NullPointerException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}

		Gdx.app.log("HexBuffer", "shader... "
				+ (shader.isCompiled() ? "loaded" : "failed"));
		if (!shader.isCompiled()) {
			Gdx.app.error("HexBuffer", shader.getLog());
		}

		// set u the verticies;
		bufferVerts = new float[vertexCount * VERTEX_LENGTH];

		// set up the indices
		bufferIndex = new short[indexCount];
		for (int i = 0, c = 0; i < bufferIndex.length; c++) {
			bufferIndex[i++] = (short) (0 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (1 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (2 + (c * VERTS_PER_HEX));

			bufferIndex[i++] = (short) (0 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (2 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (3 + (c * VERTS_PER_HEX));

			bufferIndex[i++] = (short) (0 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (3 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (4 + (c * VERTS_PER_HEX));

			bufferIndex[i++] = (short) (0 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (4 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (5 + (c * VERTS_PER_HEX));

			bufferIndex[i++] = (short) (0 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (5 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (6 + (c * VERTS_PER_HEX));

			bufferIndex[i++] = (short) (0 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (6 + (c * VERTS_PER_HEX));
			bufferIndex[i++] = (short) (1 + (c * VERTS_PER_HEX));
		}

		hasBegun = false;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(float r, float g, float b, float a) {
		if (hasBegun && outlineColor.r == r && outlineColor.g == g
				&& outlineColor.b == b && outlineColor.a == a) {
			flush();
		}
		outlineColor.set(r, g, b, a);
	}

	public float getGlowStrength() {
		return glowStrength;
	}

	public void setGlowStrength(float glowStrength) {
		if (hasBegun && this.glowStrength == glowStrength) {
			flush();
		}
		this.glowStrength = glowStrength;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(float outerRadius) {
		if (hasBegun && this.outerRadius == outerRadius) {
			flush();
		}
		this.outerRadius = outerRadius;
	}

	public float getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(float innerRadius) {
		if (hasBegun && this.innerRadius == innerRadius) {
			flush();
		}
		this.innerRadius = innerRadius;
	}

	public void begin(Matrix4 projection) {
		if (hasBegun) {
			Gdx.app.error("HexBuffer", "Called begin while running");
			return;
		}
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.begin();
		shader.setUniformMatrix("u_projection", projection);
		hasBegun = true;
	}

	public void draw(HexKey hk, HexMath hm, HexDescriptor desc){
		draw(hk, hm, desc.inner, desc.outer);	
	}
	
	public void draw(HexKey hk, HexMath hm, Color center, Color edge) {
		if (!hasBegun) {
			return;
		}
		
		if(index >= bufferVerts.length){
			flush();
		}
		
		// get center
		Vector2 v = hm.hexToPixel(hk);

		// pos
		bufferVerts[index++] = v.x;
		bufferVerts[index++] = 0f;
		bufferVerts[index++] = v.y;
		// color
		bufferVerts[index++] = center.r;
		bufferVerts[index++] = center.g;
		bufferVerts[index++] = center.b;
		bufferVerts[index++] = center.a;
		// is center
		bufferVerts[index++] = v.x;
		bufferVerts[index++] = 0f;
		bufferVerts[index++] = v.y;

		// get outer points
		for (Vector2 ring : hm.getPoints(v.x, v.y)) {
			// pos
			bufferVerts[index++] = ring.x;
			bufferVerts[index++] = 0f;
			bufferVerts[index++] = ring.y;
			// color
			bufferVerts[index++] = edge.r;
			bufferVerts[index++] = edge.g;
			bufferVerts[index++] = edge.b;
			bufferVerts[index++] = edge.a;
			// is center
			bufferVerts[index++] = v.x;
			bufferVerts[index++] = 0f;
			bufferVerts[index++] = v.y;
		}

		count++;
	}

	public void flush() {
		if (hasBegun && index > 0) {
			shader.setUniformf("u_inner_radius", innerRadius);
			shader.setUniformf("u_outer_radius", outerRadius);
			shader.setUniformf("u_outline", outlineColor);
			shader.setUniformf("u_glow_strength", glowStrength);

			mesh.setVertices(bufferVerts, 0, index);
			mesh.setIndices(bufferIndex, 0, count * IND_PER_HEX);
			mesh.render(shader, GL20.GL_TRIANGLES);
			index = 0;
			count = 0;
		}

	}

	public void end() {
		if (!hasBegun) {
			Gdx.app.error("HexBuffer", "Called end while not running");
			return;
		}
		flush();
		shader.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		hasBegun = false;
	}
}
