
attribute vec2 position;
attribute vec2 uv;
varying vec2 vUv;

uniform mat3 model;

void main() {
    vUv = uv;
    vec2 pos = vec3(model * vec3(position, 1.0)).xy;
    gl_Position = vec4((pos - vec2(20,0)) / 100., 0.0, 1.0);
}
