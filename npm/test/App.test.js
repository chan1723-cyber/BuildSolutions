const { transferir, escribirArchivo } = require('../src/App');

describe('Pruebas de la función transferir', () => {
    beforeEach(() => {
        // Restablece el archivo de transacciones antes de cada prueba
        escribirArchivo({
            'juan.jose@urosario.edu.co': [{ tipo: 'deposito', monto: 100 }],
            'sara.palaciosc@urosario.edu.co': []
        });
    });

    test('Transferencia entre cuentas exitosa', () => {
        const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);
        expect(resultado.exito).toBe(true);
        expect(resultado.mensaje).toBe('Transferencia de 30 realizada correctamente de juan.jose@urosario.edu.co a sara.palaciosc@urosario.edu.co.');
    });

    test('Transferencia con saldo insuficiente', () => {
        const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 1000);
        expect(resultado.exito).toBe(false);
        expect(resultado.mensaje).toBe('Saldo insuficiente para la transferencia.');
    });
});
