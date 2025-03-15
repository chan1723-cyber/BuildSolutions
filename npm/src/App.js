const fs = require('fs');
const path = require('path');

// Ruta del archivo de transacciones
const filePath = path.join(__dirname, 'transactions.txt');

// Función para leer el archivo transactions.txt
function leerArchivo() {
    try {
        // Verifica si el archivo existe antes de leerlo
        if (!fs.existsSync(filePath)) {
            return {}; // Si no existe, devuelve un objeto vacío
        }
        
        // Lee el contenido del archivo de forma síncrona
        const data = fs.readFileSync(filePath, 'utf8');
        
        // Parsea el contenido a JSON y lo devuelve
        return JSON.parse(data);
    } catch (error) {
        console.error('Error al leer el archivo:', error);
        return {}; // En caso de error, devuelve un objeto vacío
    }
}

// Función para escribir el archivo transactions.txt
function escribirArchivo(data) {
    try {
        // Convierte el objeto JSON a una cadena formateada
        const jsonData = JSON.stringify(data, null, 2);
        
        // Escribe los datos en el archivo de forma síncrona
        fs.writeFileSync(filePath, jsonData, 'utf8');
    } catch (error) {
        console.error('Error al escribir el archivo:', error);
    }
}

// Función para calcular el saldo actual de un usuario, basado en sus transacciones
function calcularSaldo(usuario) {
    // Obtiene los datos de las transacciones
    const data = leerArchivo();
    
    // Si el usuario no tiene transacciones, su saldo es 0
    if (!data[usuario]) return 0;
    
    // Calcula el saldo sumando depósitos y restando retiros
    return data[usuario].reduce((saldo, transaccion) => {
        return saldo + transaccion.monto; // Suma si es positivo, resta si es negativo
    }, 0);
}

// Función para realizar la transferencia entre cuentas
function transferir(de, para, monto) {
    // Verifica si el monto es válido
    if (monto <= 0) {
        return { exito: false, mensaje: 'El monto debe ser mayor a 0.' };
    }
    
    // Carga los datos de transacciones
    const data = leerArchivo();
    
    // Verifica si el usuario de origen tiene fondos suficientes
    const saldoDe = calcularSaldo(de);
    if (saldoDe < monto) {
        return { exito: false, mensaje: 'Saldo insuficiente para la transferencia.' };
    }
    
    // Registra la transacción de débito para el usuario de origen
    if (!data[de]) data[de] = [];
    data[de].push({ tipo: 'retiro', monto: -monto });
    
    // Registra la transacción de crédito para el usuario de destino
    if (!data[para]) data[para] = [];
    data[para].push({ tipo: 'deposito', monto: monto });
    
    // Guarda los cambios en el archivo
    escribirArchivo(data);
    
    return {
        exito: true,
        mensaje: `Transferencia de ${monto} realizada correctamente de ${de} a ${para}.`
    };
}

// Prueba de la función transferir
const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 0);
console.log(resultado.mensaje);

// Exportar las funciones para pruebas
module.exports = { transferir, calcularSaldo, leerArchivo, escribirArchivo };
