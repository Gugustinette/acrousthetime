server {

    server_name acrousthetime.gugustinette.com;

    location / {
            proxy_pass http://localhost:10001;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection 'upgrade';
            proxy_set_header Host $host;
            proxy_cache_bypass $http_upgrade;
    }

    listen 80;
    listen [::]:80;
}
