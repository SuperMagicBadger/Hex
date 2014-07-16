attribute vec3 a_position;
attribute vec4 a_color;
attribute vec3 a_center;

uniform mat4 u_projection;

varying vec4 v_color;
varying vec2 v_position;
varying float v_dist;

void main(void) {
	v_color = a_color;

	vec2 n_position = (u_projection * vec4(a_position, 1.0)).xy;
	vec2 n_center =   (u_projection * vec4(a_center,   1.0)).xy;

	
	v_position = (n_center - n_position);
	v_dist = 1.0;
	if(a_center == a_position){
		v_dist = 0.0;
	}

	gl_Position =  u_projection * vec4(a_position, 1.0);
}
