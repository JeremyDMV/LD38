#version 330

uniform sampler2D texture_sampler;

in vec2 passTexCoord;

out vec4 out_color;

void main(){
	vec4 tex_color = texture(texture_sampler, passTexCoord);

	if(tex_color.a == 1){
		discard;
	}
	
	out_color = tex_color;
}