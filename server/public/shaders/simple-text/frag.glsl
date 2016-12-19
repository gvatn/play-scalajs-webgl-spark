precision mediump float;

uniform sampler2D map;

varying vec2 vUv;

void main() {
    vec4 texVal = texture2D(map, vUv);
    vec4 white = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 textColor = vec4(vUv.x, vUv.y, 0.8, 1.0);
    gl_FragColor = mix(white, textColor, texVal.x);
}
