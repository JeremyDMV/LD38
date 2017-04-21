#version 330

uniform sampler2D texture_sampler;

in vec2 passTexCoord;

out vec4 outColor;

void main(){
	vec4 sample_color = texture(texture_sampler, passTexCoord);
	
	if(sample_color.a == 0){
		discard;
	}
	
	outColor = sample_color;
}