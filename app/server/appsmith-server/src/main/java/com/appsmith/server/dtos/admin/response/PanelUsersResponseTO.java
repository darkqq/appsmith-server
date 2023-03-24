package com.appsmith.server.dtos.admin.response;

import com.appsmith.server.domains.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PanelUsersResponseTO {
    private List<User> users;
}
