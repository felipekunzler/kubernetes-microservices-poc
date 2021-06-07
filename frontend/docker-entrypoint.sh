if [ -n "${DEPLOY_ENV}" ]; then
  mv /usr/share/nginx/html/endpoints/${DEPLOY_ENV}.json /usr/share/nginx/html/endpoints/endpoints.json
fi
