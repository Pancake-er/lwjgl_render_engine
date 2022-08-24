#version 410
#extension GL_ARB_explicit_uniform_location : enable
#extension GL_NV_gpu_shader5 : enable  
#extension GL_ARB_bindless_texture : enable  

layout(location = 0) in vec2 v_textureCoords;
layout(location = 1) flat in uint64_t v_textureIndex;

layout(location = 0) out vec4 o_color;

void main() {
    o_color = texture(sampler2D(v_textureIndex), v_textureCoords);
}