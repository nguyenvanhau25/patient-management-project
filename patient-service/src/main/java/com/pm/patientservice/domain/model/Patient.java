    package com.pm.patientservice.domain.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotNull;
    import lombok.Data;

    import java.time.Instant;
    import java.time.LocalDate;
    import java.util.UUID;

    @Entity
    @Data
    public class Patient {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @NotNull
        private String name;

        @NotNull
        @Email
        @Column(unique = true)
        private String email;

        @NotNull
        private String address;

        private String profileImageUrl;

        @NotNull
        private LocalDate dateOfBirth;

        @NotNull
        private Instant registeredDate;

        @PrePersist
        protected void onRegister() {
            registeredDate = Instant.now();
        }

        public static Patient register(
                String name,
                String email,
                String address,
                LocalDate dob
        ) {
            Patient p = new Patient();
            p.name = name;
            p.email = email;
            p.address = address;
            p.dateOfBirth = dob;
            return p;
        }

        public void updateProfile(
                String name,
                String email,
                String address,
                LocalDate dob
        ) {
            this.name = name;
            this.email = email;
            this.address = address;
            this.dateOfBirth = dob;
        }

        public void updateProfileImage(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }
