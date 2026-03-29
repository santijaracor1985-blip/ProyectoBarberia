package com.example.application.views.barberia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.application.models.Barbero;
import com.example.application.models.Cita;
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

@Route("barberia")
public class BarberiaView extends VerticalLayout {

    private CitaService servicio = new CitaService();
    private Div contenido = new Div();
    private DatePicker fechaGlobal = new DatePicker("Fecha");

    public BarberiaView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        getElement().getThemeList().add("dark");
        getStyle()
            .set("background", "linear-gradient(135deg,#0f172a,#020617)")
            .set("color", "#f8fafc");

        add(crearHeader());

        contenido.setWidthFull();
        contenido.getStyle().set("padding", "30px");

        fechaGlobal.setValue(LocalDate.now());
        fechaGlobal.addValueChangeListener(e -> mostrarAgenda());

        mostrarAgenda();

        add(contenido);
    }

    private Component crearHeader() {
        H2 nombre = new H2("INMANTIC BARBER");
        nombre.getStyle().set("color", "#D4AF37").set("margin", "0");

        Image logo = new Image("https://static.vecteezy.com/system/resources/thumbnails/017/375/307/small_2x/a-simple-but-powerful-black-and-white-logo-depicting-a-stylish-and-brutal-man-for-your-brand-vector.jpg", "logo");
        logo.setWidth("45px");

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
            .set("color", "#e2e8f0")
            .set("border", "none")
            .set("cursor", "pointer")
            .set("font-weight", "600");

        btn.getElement().executeJs(
            "this.addEventListener('mouseover', ()=>this.style.color='#D4AF37');" +
            "this.addEventListener('mouseout', ()=>this.style.color='#e2e8f0');"
        );
        return btn;
    }

    private void mostrarAgenda() {
        contenido.removeAll();
        contenido.add(vistaAgenda());
    }

    private Component vistaAgenda() {

        TextField nombre = new TextField("Nombre");
        nombre.setWidthFull();

        // 🔥 NUEVO: SEXO
        ComboBox<String> sexo = new ComboBox<>("Sexo");
        sexo.setItems("Masculino", "Femenino", "Otro");
        sexo.setWidthFull();

        HorizontalLayout servicios = new HorizontalLayout();
        servicios.setSpacing(true);

        String[] tipos = {"corte","barba","cejas","tinte","freestyle"};
        final String[] seleccionado = {null};

        ComboBox<Barbero> barberoBox = new ComboBox<>("Barbero");
        barberoBox.setWidthFull();
        barberoBox.setItems(servicio.obtenerBarberos());
        barberoBox.setItemLabelGenerator(b -> b.getNombre() + " - " + b.getEspecialidades());

        for (String tipo : tipos) {
            Button chip = new Button(formato(tipo));
            chip.getStyle()
                .set("background", "#1e293b")
                .set("color", "#e2e8f0")
                .set("border-radius", "20px")
                .set("padding", "8px 15px")
                .set("cursor", "pointer");

            chip.addClickListener(e -> {
                seleccionado[0] = tipo;
                servicios.getChildren().forEach(c -> 
                    c.getElement().getStyle().set("background", "#1e293b")
                );
                chip.getStyle().set("background", "#D4AF37").set("color", "black");

                List<Barbero> lista = servicio.obtenerBarberosPorServicio(tipo);
                barberoBox.setItems(lista);

                if (lista.isEmpty()) Notification.show("❌ No hay barberos disponibles");
            });

            servicios.add(chip);
        }

        fechaGlobal.setWidthFull();

        ComboBox<String> hora = new ComboBox<>("Hora");
        hora.setItems("10:00","11:00","12:00","14:00","15:00");
        hora.setWidthFull();

        Button btn = new Button("Agendar cita");
        btn.setWidthFull();
        btn.getStyle()
            .set("background", "#D4AF37")
            .set("color", "#000")
            .set("border-radius", "10px")
            .set("cursor", "pointer")
            .set("font-weight", "600");

        btn.addClickListener(e -> {
            try {
                if (seleccionado[0] == null) {
                    Notification.show("Selecciona un servicio");
                    return;
                }

                if (barberoBox.getValue() == null) {
                    Notification.show("Selecciona un barbero");
                    return;
                }

                // 🔥 VALIDACIÓN SEXO
                if (sexo.getValue() == null) {
                    Notification.show("Selecciona el sexo");
                    return;
                }

                LocalDateTime fechaHora = LocalDateTime.of(
                    fechaGlobal.getValue(),
                    LocalTime.parse(hora.getValue())
                );

                servicio.agendar(
                    nombre.getValue(),
                    "000",
                    sexo.getValue(), // 🔥 NUEVO
                    seleccionado[0],
                    fechaHora
                );

                Notification.show("✅ Cita agendada");

                mostrarAgenda();

            } catch (Exception ex) {
                Notification.show(ex.getMessage());
            }
        });

        FormLayout form = new FormLayout(
            nombre,
            sexo, // 🔥 NUEVO
            new Div(new Span("Servicio"), servicios),
            barberoBox,
            fechaGlobal,
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

        HorizontalLayout barberoCards = new HorizontalLayout();
        barberoCards.getStyle().set("display", "flex");
        barberoCards.getStyle().set("flex-wrap", "wrap");
        barberoCards.getStyle().set("gap", "20px");
        barberoCards.setWidthFull();

        for (Barbero b : servicio.obtenerBarberos()) {
            barberoCards.add(crearTarjetaBarbero(b));
        }

        VerticalLayout layout = new VerticalLayout(
            new H2("Agendar Cita"),
            card,
            new H2("Nuestros Barberos"),
            barberoCards
        );
        layout.setPadding(false);
        layout.setSpacing(true);

        return layout;
    }

    // 🔽 TODO LO DEMÁS IGUAL (NO SE TOCÓ)

    private VerticalLayout crearTarjetaBarbero(Barbero barbero) {
        H4 nombre = new H4(barbero.getNombre());
        nombre.getStyle().set("color","#D4AF37");

        Span especialidades = new Span(String.join(", ", barbero.getEspecialidades()));
        especialidades.getStyle().set("color","#e2e8f0");

        VerticalLayout card = new VerticalLayout(nombre, especialidades);
        card.getStyle()
            .set("background","#1e293b")
            .set("padding","15px")
            .set("border-radius","15px")
            .set("box-shadow","0 10px 20px rgba(0,0,0,0.6)")
            .set("min-width","180px")
            .set("text-align","center")
            .set("cursor","pointer");

        Div agendaDiv = new Div();
        agendaDiv.getStyle()
            .set("margin-top", "10px")
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "5px");

        List<LocalTime> horas = List.of(
            LocalTime.of(10,0),
            LocalTime.of(11,0),
            LocalTime.of(12,0),
            LocalTime.of(14,0),
            LocalTime.of(15,0)
        );

        LocalDateTime hoy = LocalDateTime.of(fechaGlobal.getValue(), LocalTime.of(0,0));

        List<Cita> citasHoy = servicio.obtenerCitasPorBarbero(barbero).stream()
            .filter(c -> c.getFechaHora().toLocalDate().equals(hoy.toLocalDate()))
            .collect(Collectors.toList());

        for (LocalTime h : horas) {
            boolean ocupado = citasHoy.stream().anyMatch(c -> c.getFechaHora().toLocalTime().equals(h));
            Span horaSpan = new Span(h + " - " + (ocupado ? "❌ Ocupada" : "✅ Disponible"));
            horaSpan.getStyle()
                .set("padding", "2px 5px")
                .set("border-radius", "5px")
                .set("color", "#fff")
                .set("background", ocupado ? "#991b1b" : "#16a34a")
                .set("font-size", "12px")
                .set("text-align", "center");

            agendaDiv.add(horaSpan);
        }

        card.add(agendaDiv);
        return card;
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

        Grid<Factura> grid = new Grid<>(Factura.class, false);
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

    // 🖼️ GALERÍA PREMIUM DE CORTES
    private void mostrarGaleria() {
        contenido.removeAll();

        H2 titulo = new H2("Galería y Presentación de Nuestros Barberos");
        titulo.getStyle()
            .set("color", "#D4AF37")
            .set("text-align", "center")
            .set("margin-bottom", "30px");

        HorizontalLayout fila = new HorizontalLayout();
        fila.getStyle()
            .set("flex-wrap", "wrap")
            .set("gap", "20px")
            .set("justify-content", "center");

        for (Barbero b : servicio.obtenerBarberos()) {
            fila.add(crearCardBarberoAvanzada(b));
        }

        contenido.add(new VerticalLayout(titulo, fila));
    }

    private VerticalLayout crearCardBarberoAvanzada(Barbero barbero) {
        Image img = new Image("https://i.imgur.com/3ZQ3Z4L.jpg", barbero.getNombre());
        img.setWidth("250px");
        img.getStyle().set("border-radius", "15px");

        H4 nombre = new H4(barbero.getNombre());
        nombre.getStyle().set("color", "#D4AF37").set("margin", "5px 0");

        Span especialidades = new Span("Especialidades: " + String.join(", ", barbero.getEspecialidades()));
        especialidades.getStyle().set("color", "#e2e8f0").set("font-size", "14px");

        Span presentacion = new Span("¡Hola! Soy " + barbero.getNombre() + ", experto en cortes y estilo. Aquí algunos de mis trabajos:");
        presentacion.getStyle()
            .set("color", "#cbd5e1")
            .set("font-size", "13px")
            .set("display", "block")
            .set("margin-bottom", "10px");

        HorizontalLayout galeria = new HorizontalLayout();
        galeria.getStyle()
            .set("gap", "10px")
            .set("overflow-x", "auto")
            .set("padding", "5px 0");
        galeria.setWidthFull();

        String[] ejemplosUrls = {
            "https://i.imgur.com/3ZQ3Z4L.jpg",
            "https://i.imgur.com/JQ9Z4ZT.jpg",
            "https://i.imgur.com/8Km9tLL.jpg",
            "https://i.imgur.com/W1T7CXd.jpg",
            "https://i.imgur.com/kpXZP0N.jpg"
        };

        for (String url : ejemplosUrls) {
            Image ej = new Image(url, "Ejemplo de corte");
            ej.setWidth("70px");
            ej.getStyle().set("border-radius", "8px").set("cursor", "pointer");

            ej.addClickListener(ev -> {
                Dialog modal = new Dialog();
                modal.setWidth("80%");
                modal.setHeight("80%");

                Image grande = new Image(url, "Corte de " + barbero.getNombre());
                grande.setWidth("100%");
                grande.setHeight("100%");
                grande.getStyle().set("object-fit", "contain");

                modal.add(grande);
                modal.open();
            });

            galeria.add(ej);
        }

        VerticalLayout card = new VerticalLayout(img, nombre, especialidades, presentacion, galeria);
        card.getStyle()
            .set("background","#1e293b")
            .set("padding","15px")
            .set("border-radius","15px")
            .set("box-shadow","0 10px 25px rgba(0,0,0,0.7)")
            .set("text-align","center")
            .set("min-width","280px");

        return card;
    }
}