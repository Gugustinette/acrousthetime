# Deploy the app from scratch
# Usage: ./deploy/scripts/deploy.sh

# Add Nginx configuration
sudo cp deploy/acrousthetime.gugustinette.com /etc/nginx/sites-available/acrousthetime.gugustinette.com
sudo cp deploy/api.acrousthetime.gugustinette.com /etc/nginx/sites-available/api.acrousthetime.gugustinette.com
sudo ln -s /etc/nginx/sites-available/acrousthetime.gugustinette.com /etc/nginx/sites-enabled/acrousthetime.gugustinette.com
sudo ln -s /etc/nginx/sites-available/api.acrousthetime.gugustinette.com /etc/nginx/sites-enabled/api.acrousthetime.gugustinette.com

# Verify Nginx configuration
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx
