#  Sistema de Gestión de Citas - Barbería (Java + Vaadin)

##  Descripción del proyecto

Este proyecto es una aplicación desarrollada en Java que permite gestionar citas en una barbería. El sistema permite registrar clientes, seleccionar servicios, asignar barberos y generar facturas automáticamente. Está diseñado para simular un entorno real de agendamiento, aplicando buenas prácticas de desarrollo y organización del código.

La aplicación funciona en memoria, lo que significa que los datos se almacenan durante la ejecución sin necesidad de una base de datos externa. Su objetivo principal es reforzar la lógica de programación y el uso correcto de la Programación Orientada a Objetos en un caso práctico.

---

## Programación Orientada a Objetos (POO)

El proyecto aplica varios principios fundamentales de la POO para estructurar el sistema de forma clara y mantenible.

Se utiliza el encapsulamiento para proteger los datos de las clases, permitiendo acceder a ellos únicamente mediante métodos controlados. Esto evita modificaciones incorrectas y mantiene la integridad de la información.

La herencia permite reutilizar código entre clases relacionadas, facilitando la organización y evitando duplicaciones innecesarias. A través del uso de super se accede a constructores o comportamientos de clases padre.

También se implementan interfaces para definir comportamientos que deben cumplir ciertas clases, como es el caso de la gestión de citas. Esto permite que el sistema sea más flexible y escalable.

El polimorfismo permite trabajar con diferentes objetos de manera general, facilitando la extensión del sistema. Además, la sobrecarga de métodos permite manejar distintas formas de ejecutar una misma acción dependiendo de los parámetros.

---

##  Lógica del sistema

La lógica principal del sistema se centra en el proceso de agendamiento de citas. Cuando un usuario solicita una cita, el sistema valida los datos ingresados, como el nombre, el sexo y el barbero seleccionado.

Se verifica que el barbero no tenga otra cita en el mismo horario, evitando conflictos. También se permite seleccionar uno o varios servicios en una misma cita, lo que hace el sistema más flexible.

Cada servicio tiene un costo asociado y el sistema calcula automáticamente el total a pagar. Una vez validada la información, se crea la cita y se genera una factura con todos los detalles correspondientes.

Además, el sistema permite filtrar barberos según el tipo de servicio que pueden realizar, mejorando la experiencia del usuario al momento de agendar.

---

##  Interfaz con Vaadin

La interfaz gráfica está desarrollada utilizando Vaadin, lo que permite construir aplicaciones web completamente en Java sin necesidad de usar tecnologías como HTML, CSS o JavaScript.

A través de Vaadin se crean formularios para ingresar datos, seleccionar servicios y visualizar la información de citas y facturas. Esto facilita la interacción del usuario con el sistema de forma sencilla y organizada.

La integración entre la interfaz y la lógica del sistema es directa, lo que permite que los cambios en los datos se reflejen inmediatamente en la aplicación.

---


##  Conclusión

Este proyecto demuestra la aplicación práctica de la Programación Orientada a Objetos junto con lógica de negocio y una interfaz moderna utilizando Vaadin. Es una base sólida para seguir desarrollando aplicaciones más completas, integrando nuevas funcionalidades y mejorando la arquitectura del sistema.

