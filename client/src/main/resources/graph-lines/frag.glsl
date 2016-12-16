precision mediump float;

uniform sampler2D dataTexture;

const int totalPoints = 4;

void main() {
    /*gl_FragColor = vec4(gl_FragCoord.x / 600., 0.2, 1.0, 0.9);*/
    vec2 normCoord = gl_FragCoord.xy / vec2(600., 400.);
    float vals[totalPoints];
    vec3 color = vec3(0.0, 0.0, 0.0);
    for (int i = totalPoints; i >= 0; i--) {
        vec4 val = texture2D(dataTexture, vec2(normCoord.x, (float(i) / float(totalPoints))));
        float yNorm = val.x * 255. / 30.;
        yNorm += float(i) * 0.05;
        float lineSmooth = smoothstep(yNorm - 0.01, yNorm - 0.008, normCoord.y)
                            - smoothstep(yNorm + 0.008, yNorm + 0.01, normCoord.y);
        color = mix(color, vec3(0.2 * float(i), 0.2, 0.2), lineSmooth * 0.7);
        vals[i] = val.x;
    }
    gl_FragColor = vec4(color, 1.0);
}