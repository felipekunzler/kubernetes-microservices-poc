FROM node:12.21.0
WORKDIR /app

COPY . /app/
RUN npm install

CMD npm run ng serve -- --host 0.0.0.0 -c docker
