package com.company.student_management.view.room;

import com.company.student_management.entity.Room;
import com.company.student_management.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "rooms", layout = MainView.class)
@ViewController("Room.list")
@ViewDescriptor("room-list-view.xml")
@LookupComponent("roomsDataGrid")
@DialogMode(width = "64em")
public class RoomListView extends StandardListView<Room> {
}