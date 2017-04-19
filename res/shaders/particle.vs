#version 330

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform float particleZ;
uniform vec2 particlePos;

//vertex attribs
layout(location = 0) in vec3 IN_POSITION; //the vertex position
layout(location = 1) in vec2 IN_TEXCOORD; //the texture coordinate

void main(){
	
	gl_Position = projection * view * model * (vec4(IN_POSITION, 0) + vec4(particlePos.x, particlePos.y, particleZ, 0));
}