package com.zam.sidik_sumenep.roodiskusi;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_sumenep.BaseApiResponse;
import com.zam.sidik_sumenep.util.User;

public class LoginApiResponse extends BaseApiResponse {
    public List<User> member = new ArrayList<>();
}
