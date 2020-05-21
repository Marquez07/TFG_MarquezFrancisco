/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name = "PRODUCTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p")
    , @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio")
    , @NamedQuery(name = "Producto.findBySeccion", query = "SELECT p FROM Producto p WHERE p.seccion = :seccion")
    , @NamedQuery(name = "Producto.findByPasillo", query = "SELECT p FROM Producto p WHERE p.pasillo = :pasillo")
    , @NamedQuery(name = "Producto.findByEspacio", query = "SELECT p FROM Producto p WHERE p.espacio = :espacio")
    , @NamedQuery(name = "Producto.findByCantidadProducto", query = "SELECT p FROM Producto p WHERE p.cantidadProducto = :cantidadProducto")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRECIO")
    private BigDecimal precio;
    @Basic(optional = false)
    @Column(name = "SECCION")
    private String seccion;
    @Basic(optional = false)
    @Column(name = "PASILLO")
    private String pasillo;
    @Basic(optional = false)
    @Column(name = "ESPACIO")
    private String espacio;
    @Basic(optional = false)
    @Column(name = "CANTIDAD_PRODUCTO")
    private int cantidadProducto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nombrePlanta")
    private Collection<Pedido> pedidoCollection;

    public Producto() {
    }

    public Producto(String nombre) {
        this.nombre = nombre;
    }

    public Producto(String nombre, BigDecimal precio, String seccion, String pasillo, String espacio, int cantidadProducto) {
        this.nombre = nombre;
        this.precio = precio;
        this.seccion = seccion;
        this.pasillo = pasillo;
        this.espacio = espacio;
        this.cantidadProducto = cantidadProducto;
    }

    public Producto(String text, int cantidadProducto, BigDecimal precioProducto) {
        this.nombre = text;
        this.cantidadProducto = cantidadProducto;
        this.precio = precioProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getPasillo() {
        return pasillo;
    }

    public void setPasillo(String pasillo) {
        this.pasillo = pasillo;
    }

    public String getEspacio() {
        return espacio;
    }

    public void setEspacio(String espacio) {
        this.espacio = espacio;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    @XmlTransient
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Producto[ nombre=" + nombre + " ]";
    }
    
}
