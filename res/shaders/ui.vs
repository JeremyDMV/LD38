#version 330 

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_texcoord;

uniform mat4 projection;
uniform mat4 model;

uniform int SUB_TEXTURE;

uniform vec2 SUB_SIZE;
uniform vec2 TEXTURE_SIZE;
uniform vec2 TEXTURE_OFFSET;
uniform vec2 TEXTURE_REPEAT; //TODO

out vec2 passTexCoord;

void main(){
	gl_Position = projection * model * vec4(in_position, 1.0);
	vec2 MOD_TEXCOORD = in_texcoord;
	
	if(SUB_TEXTURE == 1){
		MOD_TEXCOORD = ((TEXTURE_OFFSET / TEXTURE_SIZE) + (SUB_SIZE / TEXTURE_SIZE) * MOD_TEXCOORD);
	}
	
	passTexCoord = MOD_TEXCOORD;
}