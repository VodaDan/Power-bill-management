Write-Host "Stopping and removing old containers..."
docker compose down --remove-orphans

Write-Host "Building and starting fresh..."
docker compose up --build  --force-recreate