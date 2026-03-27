package com.example.chocolateria.modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class clienteModelo {

    private final SimpleIntegerProperty idCliente;
    private final SimpleStringProperty  nombre;
    private final SimpleStringProperty  apellido;
    private final SimpleStringProperty  cedula;
    private final SimpleStringProperty  email;
    private final SimpleStringProperty  telefono;
    private final SimpleStringProperty  direccion;
    private final SimpleStringProperty  estado;

    // ── Constructor ────────────────────────────────────────────────────────────
    public clienteModelo(int idCliente, String nombre, String apellido, String cedula,
                         String email, String telefono, String direccion, String estado) {
        this.idCliente  = new SimpleIntegerProperty(idCliente);
        this.nombre     = new SimpleStringProperty(nombre);
        this.apellido   = new SimpleStringProperty(apellido);
        this.cedula     = new SimpleStringProperty(cedula);
        this.email      = new SimpleStringProperty(email);
        this.telefono   = new SimpleStringProperty(telefono);
        this.direccion  = new SimpleStringProperty(direccion);
        this.estado     = new SimpleStringProperty(estado);
    }

    // ── Getters ────────────────────────────────────────────────────────────────
    public int    getIdCliente()  { return idCliente.get();  }
    public String getNombre()     { return nombre.get();     }
    public String getApellido()   { return apellido.get();   }
    public String getCedula()     { return cedula.get();     }
    public String getEmail()      { return email.get();      }
    public String getTelefono()   { return telefono.get();   }
    public String getDireccion()  { return direccion.get();  }
    public String getEstado()     { return estado.get();     }

    // ── Setters ────────────────────────────────────────────────────────────────
    public void setNombre(String v)    { nombre.set(v);    }
    public void setApellido(String v)  { apellido.set(v);  }
    public void setCedula(String v)    { cedula.set(v);    }
    public void setEmail(String v)     { email.set(v);     }
    public void setTelefono(String v)  { telefono.set(v);  }
    public void setDireccion(String v) { direccion.set(v); }
    public void setEstado(String v)    { estado.set(v);    }

    // ── Properties (para TableView binding) ───────────────────────────────────
    public SimpleIntegerProperty idClienteProperty()  { return idCliente;  }
    public SimpleStringProperty  nombreProperty()     { return nombre;     }
    public SimpleStringProperty  apellidoProperty()   { return apellido;   }
    public SimpleStringProperty  cedulaProperty()     { return cedula;     }
    public SimpleStringProperty  emailProperty()      { return email;      }
    public SimpleStringProperty  telefonoProperty()   { return telefono;   }
    public SimpleStringProperty  direccionProperty()  { return direccion;  }
    public SimpleStringProperty  estadoProperty()     { return estado;     }
}