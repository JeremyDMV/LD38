#version 330

uniform sampler2D texture_sampler;

in vec2 OUT_TEXCOORD;

out vec4 out_color;

void main(){
	vec4 tex_color = texture(texture_sampler, OUT_TEXCOORD);
	if(tex_color.a == 0){
		discard;		
	}
	out_color = tex_color;
}