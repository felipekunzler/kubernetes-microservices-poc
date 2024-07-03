FROM node:22.4.0 as build
WORKDIR /app
COPY . /app/
RUN npm install
RUN npm run build

FROM nginx
COPY --from=build /app/dist/store /usr/share/nginx/html
COPY ./angular-nginx.conf /etc/nginx/conf.d/default.conf
COPY ./docker-entrypoint.sh /docker-entrypoint.d/
