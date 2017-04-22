#version 330

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_texcoord;

uniform mat4 PROJECTION_MAT; //othrographic
uniform mat4 VIEW_MAT; //offset of the camera
uniform mat4 MODEL_MAT; //object's positioning

uniform int SUB_TEXTURE;
uniform int FLIP_X;
uniform int FLIP_Y;

uniform vec2 SUB_SIZE;
uniform vec2 TEXTURE_SIZE;
uniform vec2 TEXTURE_OFFSET;
uniform vec2 TEXTURE_REPEAT = vec2(1); //TODO

out vec2 OUT_TEXCOORD;

void main(){
	gl_Position = PROJECTION_MAT * VIEW_MAT * MODEL_MAT * vec4(in_position, 1.0);
		
 	vec2 MOD_TEXCOORD = in_texcoord;
	
 	if(FLIP_X == 1){
 		MOD_TEXCOORD.x = 1 - MOD_TEXCOORD.x;
 	}
 	
 	if(FLIP_Y == 1){
 		MOD_TEXCOORD.y = 1 - MOD_TEXCOORD.y;
 	}
 	
 	if(SUB_TEXTURE == 1){
 		MOD_TEXCOORD = ((TEXTURE_OFFSET / TEXTURE_SIZE) + (SUB_SIZE / TEXTURE_SIZE) * MOD_TEXCOORD);
 	}
 	
	OUT_TEXCOORD = MOD_TEXCOORD; 	
}