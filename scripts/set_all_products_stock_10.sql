-- Script: set_all_products_stock_10.sql
-- Uso: ejecutar en la base de datos del proyecto (MySQL) para fijar stock=10 en todos los productos
-- Ejemplo en phpMyAdmin: abra la base de datos -> SQL -> pegue este contenido -> ejecutar

UPDATE productos
SET stock = 10;

-- Opcional: verificar conteo de filas actualizadas
SELECT COUNT(*) AS total_products, SUM(stock) AS total_stock FROM productos;
