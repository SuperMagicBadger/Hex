#ifdef GL_ES
precision lowp float;
#endif

uniform mat4 u_projection;
uniform vec4 u_outline;
uniform float u_inner_radius;
uniform float u_outer_radius;
uniform float u_glow_strength;

varying vec4 v_color;
varying vec2 v_position;
varying float v_dist;


float exstep(float low, float high, float x){
	if(x < low) {
		return 0;
	}
	if(x > high) {
		return 0;
	}
	return 	(1 / pow(high - low, 3)) * pow((x - low), 3);
}

void main(void) {

	// center
	gl_FragColor = vec4(
		v_color.rgb, 
		mix(
			exstep(u_inner_radius, u_outer_radius, v_dist),
			v_color.a, 
			smoothstep(u_inner_radius, u_outer_radius, v_dist) * 0.75
		)
	);
	
	// ouline
	if(v_dist > u_outer_radius){
		gl_FragColor = 
			mix(
				vec4(0, 0, 0, 1), 
				u_outline, 
				smoothstep(u_outer_radius, 1, v_dist)
			);
		gl_FragColor =
			mix(
				gl_FragColor,
				vec4(1, 1, 1, 1),
				smoothstep(u_outer_radius, 1, v_dist) * u_glow_strength
			);
	}
}

