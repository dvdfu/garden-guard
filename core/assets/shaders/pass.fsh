uniform sampler2D u_texture;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
	vec4 color = texture2D(u_texture, v_texCoords) * v_color;
	gl_FragColor = color;
}