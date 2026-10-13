# Playwright

## Team 2

- Bjarki Baldursson Harksen (bbh14@hi.is) -- [@mrbjarksen](https://github.com/mrbjarksen)
- Alexander Bjartur Guðbjartsson (abg63@hi.is) -- [@svartursandur](https://github.com/svartursandur)
- Hermann Freyr Guðmundsson (hfg8@hi.is) -- [@hfg88](https://github.com/hfg88)
- Ágúst Sigurjónsson (ags15@hi.is) -- [@Agustsig](https://github.com/ags15-hi)

## Implemented endpoints

- `POST /api/users/register`
  - Expected body: `{"username": "<username>", "password": "<password>"}`
- `POST /api/users/login`
  - Expected body: `{"username": "<username>", "password": "<password>"}`
- `GET /api/users/{userId}`
- `GET /api/users?username={username}`
- `DELETE /api/users/{userId}`
- `POST /api/tracks?file=<file>`
  - Expected content type: `multipart/form-data`
- `GET /api/tracks`
- `GET /api/tracks/{trackId}`
- `GET /api/tracks/{trackId}/file`
- `DELETE /api/tracks/{trackId}`
