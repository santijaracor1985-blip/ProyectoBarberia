package com.example.application.views.barberia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.application.models.Barbero;
import com.example.application.models.Cita;
import com.example.application.models.Factura;
import com.example.application.services.CitaService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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
        // --- 1. CORRECCIÓN DE FONDO GLOBAL ---
        // Forzamos al HTML y al BODY a tener el fondo oscuro para evitar el espacio blanco
        UI.getCurrent().getElement().executeJs(
            "document.documentElement.style.backgroundColor = '#020617';" +
            "document.body.style.backgroundColor = '#020617';"
        );

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getElement().getThemeList().add("dark");

        // Estilo del layout principal con degradado fijo
        getStyle()
            .set("background", "linear-gradient(135deg,#0f172a,#020617)")
            .set("background-attachment", "fixed")
            .set("min-height", "100vh")
            .set("color", "#f8fafc")
            .set("margin", "0");

        add(crearHeader());

        contenido.setWidthFull();
        contenido.getStyle()
            .set("padding", "30px")
            .set("flex-grow", "1"); // Empuja el fondo hacia abajo

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
            .set("box-shadow", "0 4px 20px rgba(0,0,0,0.6)")
            .set("z-index", "10");

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

        ComboBox<String> sexo = new ComboBox<>("Sexo");
        sexo.setItems("Masculino"); 
        sexo.setValue("Masculino"); 
        sexo.setWidthFull();

        HorizontalLayout servicios = new HorizontalLayout();
        servicios.setSpacing(true);

        String[] tipos = {"corte","barba","cejas","tinte","freestyle"};
        final List<String> seleccionados = new ArrayList<>();

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
                if (seleccionados.contains(tipo)) {
                    seleccionados.remove(tipo);
                    chip.getStyle().set("background", "#1e293b").set("color", "#e2e8f0");
                } else {
                    seleccionados.add(tipo);
                    chip.getStyle().set("background", "#D4AF37").set("color", "black");
                }

                List<Barbero> lista = servicio.obtenerBarberos().stream()
                    .filter(b -> b.getEspecialidades().containsAll(seleccionados))
                    .collect(Collectors.toList());

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
                if (seleccionados.isEmpty()) { Notification.show("Selecciona al menos un servicio"); return; }
                if (barberoBox.getValue() == null) { Notification.show("Selecciona un barbero"); return; }
                if (hora.getValue() == null) { Notification.show("Selecciona la hora"); return; }

                LocalDateTime fechaHora = LocalDateTime.of(fechaGlobal.getValue(), LocalTime.parse(hora.getValue()));
                servicio.agendar(nombre.getValue(), "000", sexo.getValue(), String.join(", ", seleccionados), fechaHora, barberoBox.getValue());
                Notification.show("✅ Cita agendada");
                mostrarAgenda();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });

        FormLayout form = new FormLayout(nombre, sexo, new Div(new Span("Servicio"), servicios), barberoBox, fechaGlobal, hora, btn);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1), new FormLayout.ResponsiveStep("600px",2));

        Div card = new Div(form);
        card.setWidth("600px");
        card.getStyle().set("margin","0 auto").set("background","#020617").set("padding","30px").set("border-radius","15px").set("box-shadow","0 20px 40px rgba(0,0,0,0.7)");

        HorizontalLayout barberoCards = new HorizontalLayout();
        barberoCards.getStyle().set("display", "flex").set("flex-wrap", "wrap").set("gap", "20px");
        barberoCards.setWidthFull();

        for (Barbero b : servicio.obtenerBarberos()) {
            barberoCards.add(crearTarjetaBarbero(b));
        }

        VerticalLayout layout = new VerticalLayout(new H2("Agendar Cita"), card, new H2("Nuestros Barberos"), barberoCards);
        layout.setPadding(false);
        layout.setSpacing(true);
        return layout;
    }

    private VerticalLayout crearTarjetaBarbero(Barbero barbero) {
        H4 nombre = new H4(barbero.getNombre());
        nombre.getStyle().set("color","#D4AF37");
        Span especialidades = new Span(String.join(", ", barbero.getEspecialidades()));
        especialidades.getStyle().set("color","#e2e8f0");

        VerticalLayout card = new VerticalLayout(nombre, especialidades);
        card.getStyle().set("background","#1e293b").set("padding","15px").set("border-radius","15px").set("box-shadow","0 10px 20px rgba(0,0,0,0.6)").set("min-width","180px").set("text-align","center");

        Div agendaDiv = new Div();
        agendaDiv.getStyle().set("margin-top", "10px").set("display", "flex").set("flex-direction", "column").set("gap", "5px");

        List<LocalTime> horas = List.of(LocalTime.of(10,0), LocalTime.of(11,0), LocalTime.of(12,0), LocalTime.of(14,0), LocalTime.of(15,0));
        LocalDate fechaActual = fechaGlobal.getValue();

        List<Cita> citasHoy = servicio.obtenerCitasPorBarbero(barbero).stream()
            .filter(c -> c.getFechaHora().toLocalDate().equals(fechaActual))
            .collect(Collectors.toList());

        for (LocalTime h : horas) {
            boolean ocupado = citasHoy.stream().anyMatch(c -> c.getFechaHora().toLocalTime().equals(h));
            Span horaSpan = new Span(h + " - " + (ocupado ? "❌ Ocupada" : "✅ Disponible"));
            horaSpan.getStyle()
                .set("padding", "2px 5px")
                .set("border-radius", "5px")
                .set("color", "#fff")
                .set("background", ocupado ? "#991b1b" : "#16a34a")
                .set("font-size", "12px");
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

    private void mostrarFacturas() {
        contenido.removeAll();
        Grid<Factura> grid = new Grid<>(Factura.class, false);
        grid.addColumn(Factura::getCliente).setHeader("Cliente");
        grid.addColumn(Factura::getServicio).setHeader("Servicio");
        grid.addColumn(f -> (int) f.getPrecio()).setHeader("Total ($)");
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

    private void mostrarGaleria() {
        contenido.removeAll();
        H2 titulo = new H2("Nuestros Barberos y su Trabajo");
        titulo.getStyle().set("color", "#D4AF37").set("text-align", "center").set("margin-bottom", "30px");

        HorizontalLayout fila = new HorizontalLayout();
        fila.getStyle().set("flex-wrap", "wrap").set("gap", "30px").set("justify-content", "center");

        for (Barbero b : servicio.obtenerBarberos()) {
            fila.add(crearCardBarberoAvanzada(b));
        }
        contenido.add(new VerticalLayout(titulo, fila));
    }

    private VerticalLayout crearCardBarberoAvanzada(Barbero barbero) {
        H4 nombre = new H4(barbero.getNombre());
        nombre.getStyle().set("color", "#D4AF37").set("margin", "0").set("font-size", "1.6em");

        Span especialidades = new Span("Especialista en: " + String.join(", ", barbero.getEspecialidades()));
        especialidades.getStyle().set("color", "#e2e8f0").set("font-size", "14px").set("margin-bottom", "5px");

        Span presentacion = new Span("Explora los últimos estilos realizados por " + barbero.getNombre() + ":");
        presentacion.getStyle().set("color", "#cbd5e1").set("font-size", "13px").set("margin-bottom", "15px");

        HorizontalLayout galeria = new HorizontalLayout();
        galeria.getStyle().set("gap", "15px").set("justify-content", "center").set("padding", "10px 0");
        galeria.setWidthFull();

        String[] ejemplosUrls;
        switch (barbero.getNombre().toLowerCase()) {
            case "carlos freestyle":
                ejemplosUrls = new String[]{"https://i.pinimg.com/736x/f6/b3/d4/f6b3d46e0e040d3838e06bc5926994fc.jpg", "https://i.pinimg.com/originals/e3/65/98/e365982e9c540003bca765ea6af566bf.jpg", "https://i.pinimg.com/736x/93/4f/28/934f2820409c608190b930b027bb4b6d.jpg"};
                break;
            case "andrés clasico":
                ejemplosUrls = new String[]{"https://i.pinimg.com/originals/88/54/7c/88547c9dd7264aa49958979cb4ae4e63.jpg", "https://i.pinimg.com/originals/b2/cf/95/b2cf95a55d0b602cac6ed53b8eae18f2.jpg", "https://i.pinimg.com/originals/b4/a8/34/b4a834d2ee54f75ab2fbf3ea58d442fc.jpg"};
                break;
            case "luis colorista":
                ejemplosUrls = new String[]{"https://i.pinimg.com/originals/e1/af/1d/e1af1ddb014a4df3960362e7d28afb39.jpg", "https://i.pinimg.com/736x/9f/3c/75/9f3c75f372fd5a3cb73ab657d22d209d.jpg", "https://i.pinimg.com/550x/2c/5a/ac/2c5aacc17393e4ba05938fa556983336.jpg"};
                break;
            case "miguel full":
                ejemplosUrls = new String[]{"https://i.pinimg.com/originals/c1/50/2f/c1502f1695f322ef10368ce71ab12381.jpg", "https://cortesdepelohombres.com/wp-content/uploads/2019/12/Corte-de-pelo-con-barba-con-degradado.jpg", "https://i.pinimg.com/736x/73/13/e1/7313e1e318567b336bcdb80d27e24982.jpg"};
                break;
            default:
                ejemplosUrls = new String[]{};
        }

        for (String url : ejemplosUrls) {
            Image ej = new Image(url, "Trabajo");
            ej.setWidth("115px");
            ej.setHeight("115px");
            ej.getStyle()
                .set("border-radius", "12px")
                .set("object-fit", "cover")
                .set("transition", "transform 0.3s ease, border 0.3s");

            ej.getElement().executeJs(
                "this.addEventListener('mouseover', ()=> { this.style.transform='scale(1.1)'; this.style.border='2px solid #D4AF37'; });" +
                "this.addEventListener('mouseout', ()=> { this.style.transform='scale(1.0)'; this.style.border='none'; });"
            );

            ej.addClickListener(ev -> {
                Dialog modal = new Dialog();
                Image grande = new Image(url, "Vista completa");
                grande.setWidth("100%");
                modal.add(grande);
                modal.open();
            });
            galeria.add(ej);
        }

        VerticalLayout card = new VerticalLayout(nombre, especialidades, presentacion, galeria);
        card.getStyle()
            .set("background","#1e293b")
            .set("padding","25px")
            .set("border-radius","20px")
            .set("box-shadow","0 15px 30px rgba(0,0,0,0.8)")
            .set("text-align","center")
            .set("min-width","400px");

        return card;
    }
}