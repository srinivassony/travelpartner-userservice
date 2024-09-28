package com.travelpartner.user_service.utill;

import org.springframework.stereotype.Component;

@Component
public class HtmlTemplate {

	public String InviteUser(String id, String userName) {
		String signInUrl = "http://localhost:9093/api/v1/update/invite/user/" + id;

		return "<div style=\"width: 100% !important; height: 100%; background: #ffffff;font-size: 16px;\">"
				+ "<div style=\"display: block !important; clear: both !important; margin: 0 auto !important; max-width: 700px !important; font-size: 13px; color: #2F2D46\">"
				+ "<div style=\"padding: 40px;font-family:'Open Sans';font-weight:500;color:#2F2D46;font-size:16px;line-height:24px !important;\">"
				+ "<div style=\"margin-top: 10px;\">Hi " + userName + ",</div>"
				+ "<div style=\"margin-top: 20px;\">We are inviting you to Travel-partner.</div>"
				+ "<div style=\"margin-top: 20px;\">Sign up with this email address to start today. Please click on the below link to activate your account.</div>"
				+ "<div class=\"container\" style=\"width: 100%;margin-top: 30px;display: block;position: relative;text-align: center;\">"
				+ "<a style=\"text-decoration: none; cursor: pointer;border-radius: 25px;text-decoration: none;cursor: pointer;padding: 8px 25px;font-weight: 500;background-color: #ED8B00;color:white;\" href=\""
				+ signInUrl + "\">" + "CLICK HERE" + "</a>" + "</div>"
				+ "<hr style=\"margin-top: 40px; border-top: 1px solid #D5D1D1;\">"
				+ "<div style=\"font-size: 12px; color:#6D6C7D;font-weight: 500;line-height:14px !important;text-align: justify;\">"
				+ "<div style=\"margin-top: 10px;\">"
				+ "This is an automated message. Please do not reply directly to this email as you will not receive a response. If you have any system-related questions or technical issues, please contact us at <a style=\"color: #6B63A1; text-decoration: underline;\" href=\"mailto:#\"> XXXXX</a>."
				+ "</div>" + "<div style=\"margin-top:10px;\">&copy; " + java.time.Year.now()
				+ " TRAVEL-PARTNER. All rights reserved.</div>" + "</div>" + "</div>" + "</div>" + "</div>";
	}

}
