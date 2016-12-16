#ifdef GL_OES_standard_derivatives
#extension GL_OES_standard_derivatives : enable
#endif

precision mediump float;

uniform sampler2D map;

varying vec2 vUv;

float median(float r, float g, float b) {
    return max(min(r,g), min(max(r,g),b));
}

void main() {
    vec3 sdfVal = 1.0 - texture2D(map, vUv).rgb;
    float sigDist = median(sdfVal.r, sdfVal.g, sdfVal.b) - 0.5;
    //float alpha = clamp(sigDist/fwidth(sigDist) + 0.5, 0.0, 1.0);
    float coef = 1.0 - sigDist;
    gl_FragColor = vec4(1.0, coef, coef, 1.0);
}
