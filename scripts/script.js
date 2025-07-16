import http from 'k6/http';
import { sleep, check } from 'k6';

export let options = {
  stages: [
    { duration: '30s', target: 50 }, // sube hasta 50 VUs en 30s
    { duration: '1m', target: 50 },  // mantiene 50 VUs por 1m
    { duration: '30s', target: 0 },  // baja a 0 VUs en 30s
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% de peticiones < 500ms
  },
};

export default function () {
  let res = http.get('http://host.docker.internal:8081/api/camel-rest/jsonplaceholder/posts/1');
  check(res, {
    'status es 200': (r) => r.status === 200,
    'duración < 400ms': (r) => r.timings.duration < 400,
  });
  sleep(1); // pausa de 1 segundo entre iteraciones
}
