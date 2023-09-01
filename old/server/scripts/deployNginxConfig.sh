# Copy nginx config file to /etc/nginx/sites-available
cp ./scripts/ressources/meteo.gugustinette.com /etc/nginx/sites-available/meteo.gugustinette.com

# Create symlink to sites-enabled
ln -s /etc/nginx/sites-available/meteo.gugustinette.com /etc/nginx/sites-enabled/meteo.gugustinette.com
