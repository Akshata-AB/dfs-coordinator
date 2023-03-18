package com.scu.ds.dfs.dfscoordinator.service;

import com.scu.ds.dfs.dfscoordinator.exception.MetadataServiceException;
import com.scu.ds.dfs.dfscoordinator.model.ChunkMapping;
import com.scu.ds.dfs.dfscoordinator.model.FileMetadata;
import lombok.extern.java.Log;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;
import reactor.util.retry.RetrySpec;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Log
@Component
public class MetadataServiceImpl implements MetadataService {

    WebClient webClient;

    public MetadataServiceImpl(@Value("${metadata.base.url}") String metadataBaseUrl) {
        webClient = WebClient
                .builder()
                .baseUrl(metadataBaseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public FileMetadata getFileMetadata(MultipartFile file) {

        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
        bodyValues.add("fileName", file.getName());
        bodyValues.add("fileSize", Long.toString(file.getSize()));

        return webClient.post()
                .uri("/metadata/upload")
                .body(BodyInserters.fromFormData(bodyValues))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(this::handleGetFileMetadataResponse)
                .retryWhen(buildRetrySpec())
                .block();
    }

    @Override
    public ChunkMapping getChunkDetailsForDownload(String fileName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/metadata/download/{filename}").build(fileName))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(this::handleDownloadResponse)
                .retryWhen(buildRetrySpec())
                .block();


    }

    @Override
    public ChunkMapping getChunkDetailsForDelete(String fileName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/metadata/delete/{filename}").build(fileName))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(this::handleDownloadResponse)
                .retryWhen(buildRetrySpec())
                .block();
    }

    private Mono<FileMetadata> handleGetFileMetadataResponse(ClientResponse clientResponse) {
        log.info("Handle metadata response " + clientResponse);
        return clientResponse.bodyToMono(FileMetadata.class)
                .map(response -> {
                    if (clientResponse.statusCode().is4xxClientError()) {
                        throw new MetadataServiceException("Client error occurred while calling Metadata service", clientResponse.statusCode());
                    } else if (clientResponse.statusCode().is5xxServerError()) {
                        throw new MetadataServiceException("Server error occurred while calling Metadata service", clientResponse.statusCode());
                    }
                    return response;
                })
                .switchIfEmpty(Mono.error(new MetadataServiceException("Unknown error occurred while calling Metadata service", clientResponse.statusCode())));
    }

    private Mono<ChunkMapping> handleDownloadResponse(ClientResponse clientResponse) {
        log.info("Handle metadata response " + clientResponse);
        return clientResponse.bodyToMono(ChunkMapping.class)
                .map(response -> {
                    if (clientResponse.statusCode().is4xxClientError()) {
                        throw new MetadataServiceException("Client error occurred while calling Metadata service", clientResponse.statusCode());
                    } else if (clientResponse.statusCode().is5xxServerError()) {
                        throw new MetadataServiceException("Server error occurred while calling Metadata service", clientResponse.statusCode());
                    }
                    return response;
                })
                .switchIfEmpty(Mono.error(new MetadataServiceException("Unknown error occurred while calling Metadata service", clientResponse.statusCode())));
    }

    private RetryBackoffSpec buildRetrySpec() {
        return Retry.fixedDelay(3, Duration.ofMillis(500))
                .filter(e -> {
                    if(e instanceof MetadataServiceException) {
                        return ((MetadataServiceException)e).getStatus().is5xxServerError();
                    }
                    return true;
                })
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> retrySignal.failure()))
                .doBeforeRetry(retrySignal -> log.info("Executing retry attempt number:"+(int)(retrySignal.totalRetries() + 1)));
    }
}
