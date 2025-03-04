package com.truong.service;

import com.nimbusds.jose.crypto.MACSigner;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
@Service
public class AuthenticationService {
	private String generateToken(String username) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet().Builder()
				.subject(username)
				.issuer("truongminh.com")
				.issueTime(new Date())
				.expirationTime(new Date(
						Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
						))
				.claim("CustomClaim", "Custom")
				.build();
		Payload payload = new Payload(jwtClaimsSet.toJSONObject());
		JWSObject jwsObject = new JWSObject(header, payload);

		jwsObject.sign(new MACSigner());
	}
}
