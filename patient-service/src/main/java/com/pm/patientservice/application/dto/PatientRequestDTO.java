package com.pm.patientservice.application.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PatientRequestDTO {

  @NotBlank(message = "Tên không được để trống")
  @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
  private String name;

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotBlank(message = "Địa chỉ không được để trống")
  private String address;

  @NotBlank(message = "Ngày sinh không được để trống")
  private String dateOfBirth;

  private String profileImageUrl;
  public @NotBlank(message = "Name is required") @Size(max = 100, message = "Name cannot exceed 100 characters") String getName() {
    return name;
  }

  public void setName(
      @NotBlank(message = "Name is required") @Size(max = 100, message = "Name cannot exceed 100 characters") String name) {
    this.name = name;
  }

  public @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String getEmail() {
    return email;
  }

  public void setEmail(
      @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email) {
    this.email = email;
  }

  public @NotBlank(message = "Address is required") String getAddress() {
    return address;
  }

  public void setAddress(
      @NotBlank(message = "Address is required") String address) {
    this.address = address;
  }

  public @NotBlank(message = "Date of birth is required") String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(
      @NotBlank(message = "Date of birth is required") String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

}
