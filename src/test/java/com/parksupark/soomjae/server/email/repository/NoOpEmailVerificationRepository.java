package com.parksupark.soomjae.server.email.repository;

import com.parksupark.soomjae.server.email.entity.EmailVerification;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public class NoOpEmailVerificationRepository implements EmailVerificationRepository {

    public NoOpEmailVerificationRepository() {
        super();
    }

    @Override
    public void deleteByEmail(String email) {
    }

    @Override
    public Optional<EmailVerification> findByEmailAndExpiredAtAfter(String email, Instant now) {
        return Optional.empty();
    }

    @Override
    public Optional<EmailVerification> findByEmailAndCodeAndExpirationTimeAfter(
        String email, String code, Instant now) {
        return Optional.empty();
    }

    @Override
    public boolean existsByEmailAndVerifiedAndExpirationTimeAfter(String email, Instant now) {
        return false;
    }

    @Override
    public <S extends EmailVerification> S save(S entity) {
        return null;
    }

    @Override
    public <S extends EmailVerification> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<EmailVerification> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public List<EmailVerification> findAllById(Iterable<Long> ids) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public <S extends EmailVerification> long count(Example<S> example) {
        return 0;
    }

    @Override
    public void deleteById(Long id) {
    }

    @Override
    public void delete(EmailVerification entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
    }

    @Override
    public void deleteAll(Iterable<? extends EmailVerification> entities) {
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends EmailVerification> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends EmailVerification> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public void deleteAllInBatch(Iterable<EmailVerification> entities) {
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
    }

    @Override
    public EmailVerification getOne(Long id) {
        return null;
    }

    @Override
    public EmailVerification getById(Long id) {
        return null;
    }

    @Override
    public EmailVerification getReferenceById(Long id) {
        return null;
    }

    @Override
    public <S extends EmailVerification> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public List<EmailVerification> findAll() {
        return List.of();
    }

    @Override
    public List<EmailVerification> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<EmailVerification> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends EmailVerification> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends EmailVerification> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends EmailVerification> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends EmailVerification> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends EmailVerification, R> R findBy(
        Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
