package com.example.application.views.barberia;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.application.models.Barbero;
import com.example.application.models.Factura;
import com.example.application.services.CitaService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class BarberiaView extends VerticalLayout {

    private CitaService servicio = new CitaService();
    private Div contenido = new Div();

    public BarberiaView() {

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // 🌙 MODO OSCURO
        getElement().getThemeList().add("dark");

        getStyle()
            .set("background", "linear-gradient(135deg,#0f172a,#020617)")
            .set("color", "#f8fafc");

        add(crearHeader());

        contenido.setWidthFull();
        contenido.getStyle().set("padding", "30px");

        mostrarAgenda();

        add(contenido);
    }

    // 🔥 HEADER
    private Component crearHeader() {

        Image logo = new Image("https://cdn-icons-png.flaticon.com/512/921/921347.png", "logo");
        logo.setWidth("45px");

        H2 nombre = new H2("INMANTIC BARBER");
        nombre.getStyle()
            .set("color", "#D4AF37")
            .set("margin", "0");

        HorizontalLayout left = new HorizontalLayout(logo, nombre);
        left.setAlignItems(Alignment.CENTER);

        Button agendaBtn = crearBoton("Agenda", this::mostrarAgenda);
        Button facturasBtn = crearBoton("Facturas", this::mostrarFacturas);
        Button cortesBtn = crearBoton("Cortes", this::mostrarGaleria);

        HorizontalLayout menu = new HorizontalLayout(agendaBtn, facturasBtn, cortesBtn);

        HorizontalLayout header = new HorizontalLayout(left, menu);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        header.getStyle()
            .set("background", "#020617")
            .set("padding", "15px 40px")
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.6)");

        return header;
    }

    private Button crearBoton(String texto, Runnable accion) {
        Button btn = new Button(texto, e -> accion.run());

        btn.getStyle()
            .set("background", "transparent")
            .set("color", "#e2e8f0");

        btn.getElement().executeJs(
            "this.addEventListener('mouseover', ()=>this.style.color='#D4AF37');" +
            "this.addEventListener('mouseout', ()=>this.style.color='#e2e8f0');"
        );

        return btn;
    }

    // 📅 AGENDA
    private void mostrarAgenda() {
        contenido.removeAll();
        contenido.add(vistaAgenda());
    }

    private Component vistaAgenda() {

        TextField nombre = new TextField("Nombre");
        nombre.setWidthFull();

        // 🔥 SERVICIOS (BOTONES)
        HorizontalLayout servicios = new HorizontalLayout();
        servicios.setSpacing(true);

        String[] tipos = {"corte","barba","cejas","tinte","freestyle"};
        final String[] seleccionado = {null};

        ComboBox<Barbero> barberoBox = new ComboBox<>("Barbero");
        barberoBox.setWidthFull();

        barberoBox.setItemLabelGenerator(b ->
            b.getNombre() + " - " + b.getEspecialidades()
        );

        for (String tipo : tipos) {

            Button chip = new Button(formato(tipo));

            chip.getStyle()
                .set("background", "#1e293b")
                .set("color", "#e2e8f0")
                .set("border-radius", "20px")
                .set("padding", "8px 15px");

            chip.addClickListener(e -> {

                seleccionado[0] = tipo;

                servicios.getChildren().forEach(c ->
                    c.getElement().getStyle().set("background", "#1e293b")
                );

                chip.getStyle()
                    .set("background", "#D4AF37")
                    .set("color", "black");

                List<Barbero> lista = servicio.obtenerBarberosPorServicio(tipo);
                barberoBox.setItems(lista);

                if (lista.isEmpty()) {
                    Notification.show("❌ No hay barberos disponibles");
                }
            });

            servicios.add(chip);
        }

        // 🔥 LABEL BONITO
        Span labelServicio = new Span("Servicio");
        labelServicio.getStyle()
            .set("color", "#D4AF37")
            .set("font-weight", "600");

        DatePicker fecha = new DatePicker("Fecha");
        fecha.setWidthFull();

        ComboBox<String> hora = new ComboBox<>("Hora");
        hora.setItems("10:00","11:00","12:00","14:00","15:00");
        hora.setWidthFull();

        Button btn = new Button("Agendar cita");
        btn.setWidthFull();

        btn.getStyle()
            .set("background", "#D4AF37")
            .set("color", "#000")
            .set("border-radius", "10px");

        btn.addClickListener(e -> {
            try {

                if (seleccionado[0] == null) {
                    Notification.show("Selecciona un servicio");
                    return;
                }

                LocalDateTime fechaHora = LocalDateTime.of(
                    fecha.getValue(),
                    LocalTime.parse(hora.getValue())
                );

                servicio.agendar(nombre.getValue(),"000",seleccionado[0],fechaHora);

                Notification.show("✅ Cita agendada");

            } catch (Exception ex) {
                Notification.show(ex.getMessage());
            }
        });

        FormLayout form = new FormLayout(
            nombre,
            new Div(labelServicio, servicios),
            barberoBox,
            fecha,
            hora,
            btn
        );

        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0",1),
            new FormLayout.ResponsiveStep("600px",2)
        );

        Div card = new Div(form);
        card.setWidth("600px");

        card.getStyle()
            .set("margin","0 auto")
            .set("background","#020617")
            .set("padding","30px")
            .set("border-radius","15px")
            .set("box-shadow","0 20px 40px rgba(0,0,0,0.7)");

        return new VerticalLayout(new H2("Agendar Cita"), card);
    }

    private String formato(String t){
        switch(t){
            case "corte": return "✂️ Corte";
            case "barba": return "🧔 Barba";
            case "cejas": return "👁️ Cejas";
            case "tinte": return "🎨 Tinte";
            case "freestyle": return "🔥 Freestyle";
        }
        return t;
    }

    // 🧾 FACTURAS
    private void mostrarFacturas() {

        contenido.removeAll();

        Grid<Factura> grid = new Grid<>(Factura.class,false);

        grid.addColumn(Factura::getCliente).setHeader("Cliente");
        grid.addColumn(Factura::getServicio).setHeader("Servicio");
        grid.addColumn(Factura::getPrecio).setHeader("Total");

        grid.setItems(servicio.obtenerFacturas());

        grid.addItemClickListener(e -> {

            Dialog d = new Dialog();

            Div html = new Div();
            html.getElement().setProperty("innerHTML", e.getItem().generarHTML());

            d.add(html);
            d.open();
        });

        contenido.add(new VerticalLayout(new H2("Facturas"), grid));
    }

    // 🖼️ GALERÍA
    private void mostrarGaleria() {

        contenido.removeAll();

        HorizontalLayout fila = new HorizontalLayout();

        fila.add(
            crearCard("Freestyle","https://i.imgur.com/3ZQ3Z4L.jpg"),
            crearCard("Clásico","https://i.imgur.com/JQ9Z4ZT.jpg"),
            crearCard("Tinte","https://i.imgur.com/8Km9tLL.jpg")
        );

        contenido.add(new VerticalLayout(new H2("Cortes"), fila));
    }

    private VerticalLayout crearCard(String nombre, String url) {

        Image img = new Image(url, nombre);
        img.setWidth("200px");

        VerticalLayout card = new VerticalLayout(new H4(nombre), img);

        card.getStyle()
            .set("background", "#1e293b")
            .set("padding", "10px")
            .set("border-radius", "10px");

        return card;
    }
}