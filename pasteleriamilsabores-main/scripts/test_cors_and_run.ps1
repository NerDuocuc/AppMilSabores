Start-Process -FilePath './mvnw' -ArgumentList '-DskipTests','spring-boot:run' -NoNewWindow
$tries = 0
while(-not (Test-NetConnection -ComputerName 'localhost' -Port 8080).TcpTestSucceeded -and $tries -lt 60){
    Start-Sleep -Seconds 1
    $tries++
}
if((Test-NetConnection -ComputerName 'localhost' -Port 8080).TcpTestSucceeded){
    Write-Host "Port 8080 listening, running tests"
    Write-Host "=== PRELIGHT OPTIONS ==="
    curl -i -X OPTIONS 'http://localhost:8080/api/users/register' -H 'Origin: http://localhost:5173' -H 'Access-Control-Request-Method: POST' -H 'Access-Control-Request-Headers: content-type,authorization'
    Write-Host "=== POST REGISTER ==="
    curl -i -X POST 'http://localhost:8080/api/users/register' -H 'Origin: http://localhost:5173' -H 'Content-Type: application/json' -d '{"run":"1-9","nombre":"Test","apellidos":"User","correo":"postman-test@example.com","fechaNacimiento":"1990-01-01","tipoUsuario":"CLIENTE","password":"secret"}'
} else {
    Write-Host "Port 8080 not available after wait"
    exit 1
}