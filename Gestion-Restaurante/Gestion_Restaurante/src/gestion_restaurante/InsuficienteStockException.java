/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion_restaurante;

/**
 *
 * @author HP
 */
public class InsuficienteStockException extends RuntimeException {
    public InsuficienteStockException(String msg){
        super(msg);
    }
}
