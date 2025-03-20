package com.mobicomm.app.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentConfirmation(String toEmail, String userName, String planName, 
                                        String amount, String expiryDate, String paymentId) {
        try {
            // ✅ Format expiry date (yyyy-MM-dd)
            String formattedExpiryDate = expiryDate.split("T")[0]; 

            // ✅ Create a MimeMessage for HTML email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("📩 Payment Confirmation - MobiComm ✅");
            
            // ✅ Professional HTML Email Template
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 500px; border-radius: 10px; background-color: #f4f4f4;'>"
                    + "<div style='background-color: #007bff; color: #ffffff; text-align: center; padding: 15px; border-radius: 10px 10px 0 0;'>"
                    + "<h2 style='margin: 0;'>MobiComm Payment Confirmation</h2>"
                    + "</div>"
                    + "<div style='background-color: #ffffff; padding: 20px; border-radius: 0 0 10px 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);'>"
                    + "<p style='font-size: 16px; text-align: center;'>Dear <strong>" + userName + "</strong>,</p>"
                    + "<p style='font-size: 16px; text-align: center;'>Your payment has been successfully processed! 🎉</p>"
                    + "<div style='border-radius: 8px; background-color: #f8f9fa; padding: 15px; margin-top: 10px;'>"
                    + "<table style='width: 100%; border-collapse: collapse; font-size: 16px;'>"
                    + "<tr><td style='padding: 10px; font-weight: bold;'>📌 Payment ID:</td>"
                    + "<td style='padding: 10px; text-align: right;'>" + paymentId + "</td></tr>"
                    + "<tr><td style='padding: 10px; font-weight: bold;'>📦 Plan:</td>"
                    + "<td style='padding: 10px; text-align: right;'>" + planName + "</td></tr>"
                    + "<tr><td style='padding: 10px; font-weight: bold;'>💰 Amount Paid:</td>"
                    + "<td style='padding: 10px; text-align: right; color: #28a745;'><strong>₹" + amount + "</strong></td></tr>"
                    + "<tr><td style='padding: 10px; font-weight: bold;'>📅 Expiry Date:</td>"
                    + "<td style='padding: 10px; text-align: right; color: #dc3545;'><strong>" + formattedExpiryDate + "</strong></td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<p style='font-size: 16px; text-align: center; margin-top: 15px;'>"
                    + "Thank you for choosing <strong>MobiComm</strong>! Stay connected. 🚀"
                    + "</p>"
                    + "<p style='font-size: 14px; color: #666; text-align: center; margin-top: 15px;'>"
                    + "Need help? Contact <a href='mailto:support@mobicomm.com' style='color: #007bff; text-decoration: none;'>support@mobicomm.com</a>"
                    + "</p>"
                    + "<hr style='border-top: 1px solid #ddd;'>"
                    + "<p style='text-align: center; font-size: 12px; color: #666;'>© 2025 MobiComm Pvt Ltd. All Rights Reserved.</p>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true); // ✅ Send HTML content
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }
}
