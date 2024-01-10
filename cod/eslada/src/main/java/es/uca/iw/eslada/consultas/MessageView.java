package es.uca.iw.eslada.consultas;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import es.uca.iw.eslada.main.MainLayout;
import es.uca.iw.eslada.servicio.ServicioType;
import es.uca.iw.eslada.user.AuthenticatedUser;
import jakarta.annotation.security.RolesAllowed;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Route(value = "message", layout = MainLayout.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class MessageView extends VerticalLayout implements HasUrlParameter<String> {

    Optional<Consultation> consultation;

    ConsultationService consultationService;

    MessageService messageService;

    private AuthenticatedUser authenticatedUser;

    MessageList messageList = new MessageList();

    Collection<MessageListItem> messageListItems = new ArrayList<>();

    public MessageView(ConsultationService consultationService, AuthenticatedUser authenticatedUser,
                       MessageService messageService) {
        this.consultationService = consultationService;
        this.authenticatedUser = authenticatedUser;
        this.messageService = messageService;
        setWidthFull();
        setHeightFull();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        consultation = consultationService.findById(UUID.fromString(parameter));

        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("USER") &&
                authenticatedUser.get().get().getUsername() != consultation.get().getUser().getUsername())
            UI.getCurrent().navigate("/consultation");

        add(C1(), C2(), C3());
    }

    private HorizontalLayout C1() {
        HorizontalLayout C1 = new HorizontalLayout();
        C1.setWidthFull();
        C1.setJustifyContentMode(JustifyContentMode.BETWEEN);

        C1.add(new H1("Consulta: " + consultation.get().getId().toString()));

        if(authenticatedUser.get().get().getRoles().iterator().next().getName().matches("ADMIN")){
            Button closeConsultation = new Button("Cerrar", e -> closeConsultation(consultation.get()));
            C1.add(closeConsultation);
        }

        return C1;
    }

    private VerticalLayout C2() {
        VerticalLayout C2 = new VerticalLayout();
        C2.add(new H3(consultation.get().getName()));



        //if (!messageService.getMessagesFromConsultation(consultation.get()).isEmpty())
        for(Message msg : messageService.getMessagesFromConsultation(consultation.get())) {
            MessageListItem message = new MessageListItem(msg.getMessageString(), msg.getCreationDate().toInstant(),
                    msg.getUser().getName());
            messageListItems.add(message);
        }
        messageList.setItems(messageListItems);
        C2.add(messageList);
        return C2;
    }

    private HorizontalLayout C3() {
        HorizontalLayout C3 = new HorizontalLayout();
        MessageInput input = new MessageInput();
        input.addSubmitListener(submitEvent -> {
            Date date = new Date();

            MessageListItem newMessage = new MessageListItem(
                    submitEvent.getValue(), date.toInstant(), authenticatedUser.get().get().getName());
            List<MessageListItem> items = new ArrayList<>(messageList.getItems());
            items.add(newMessage);
            messageList.setItems(items);
            Message msg= new Message();
            msg.setConsultation(consultation.get());
            msg.setMessageString(submitEvent.getValue());
            msg.setUser(authenticatedUser.get().get());
            msg.setCreationDate(date);
            messageService.saveMessage(msg);
        });
        C3.add(input);
        return C3;
    }

    private void closeConsultation(Consultation consultation) {
        Notification notification = new Notification("Has cerrado la consulta: " + consultation.getId());
        notification.setDuration(5000);

        Dialog dialog = new Dialog();
        H2 headline = new H2("Cerrar consulta");
        dialog.add(headline);
        headline.getElement().getClassList().add("draggable");
        Text message = new Text("¿ Seguro que quieres cerrar la consulta ?");
        dialog.add(message);

        Button cancelButton = new Button("Cancelar", e -> dialog.close());
        Button closeButton = new Button("Cerrar", e -> {
            consultationService.closeConsultation(consultation);
            consultationService.findByClosed(false);
            dialog.close();
            notification.open();
            UI.getCurrent().navigate("/consultation");
        });

        closeButton.setThemeName("error");
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, closeButton);
        dialog.add(buttonLayout);

        dialog.setDraggable(true);
        dialog.setResizable(true);

        dialog.open();
    }
}
