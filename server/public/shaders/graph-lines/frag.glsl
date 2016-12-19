precision mediump float;

uniform sampler2D dataTexture;

const int totalPoints = 4;

void main() {
    /*gl_FragColor = vec4(gl_FragCoord.x / 600., 0.2, 1.0, 0.9);*/
    vec2 normCoord = gl_FragCoord.xy / vec2(800., 600.);
    float vals[totalPoints];
    vec3 color = vec3(0.0, 0.0, 0.0);
    for (int i = totalPoints; i >= 0; i--) {
        vec4 val = texture2D(dataTexture, vec2(normCoord.x, (float(i) / float(totalPoints))));
        float yNorm = val.x * 255. / 30.;
        yNorm += float(i) * 0.05;
        float lineSmooth = (smoothstep(yNorm - 0.08, yNorm - 0.01, normCoord.y) * 0.94 + 0.06)
                            - smoothstep(yNorm + 0.005, yNorm + 0.01, normCoord.y);
        float colorCoef = 2.0 - float(i) * 0.5;
        color = mix(color, vec3(0.15 * float(i), 0.2*colorCoef, 0.2*colorCoef), lineSmooth * 0.7);
        vals[i] = val.x;
    }
    gl_FragColor = vec4(color, 1.0);
}