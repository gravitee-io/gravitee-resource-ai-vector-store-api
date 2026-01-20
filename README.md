# Gravitee AI Vector Store Resource API

API definition for AI vector store resources in the Gravitee ecosystem. This module provides interfaces and contracts for implementing vector database resources that enable semantic search and similarity-based retrieval.

## Overview

This is an **API-only module** that defines the contract for vector store implementations. Concrete implementations (e.g., for Pinecone, Weaviate, Milvus, etc.) should be created in separate modules.

## Features

- Reactive API using RxJava3 for non-blocking operations
- Support for multiple similarity metrics (Euclidean, Cosine, Dot Product)
- Multiple index types (Flat, IVF, HNSW)
- Configurable eviction policies
- Metadata support for vector entities
- Spring-aware resource management

## Building

```bash
# Compile
mvn clean compile

# Package
mvn clean package

# Install to local repository
mvn clean install
```

## Core Interfaces

### VectorStore

Main interface for vector store operations:

```java
public interface VectorStore extends ApplicationContextAware {
  Completable add(VectorEntity vectorEntity);
  Flowable<VectorResult> findRelevant(VectorEntity vectorEntity);
  void remove(VectorEntity vectorEntity);
  default Completable rxRemove(VectorEntity vectorEntity) { ... }
}
```

### AiVectorStoreResource

Abstract base class for implementations:

```java
public abstract class AiVectorStoreResource<C extends ResourceConfiguration>
  extends AbstractConfigurableResource<C>
  implements VectorStore {

  public <T> T getBean(Class<T> clazz) { ... }
}
```

## Data Models

### VectorEntity

Represents a vector with associated data:

```java
public record VectorEntity(
  String id,
  String text,
  float[] vector,
  Map<String, Object> metadata,
  long timestamp
)
```

### VectorResult

Search result with similarity score:

```java
public record VectorResult(VectorEntity entity, float score)
```

### AiVectorStoreProperties

Configuration for vector store behavior:

```java
public record AiVectorStoreProperties(
  int embeddingSize,        // Dimension of vectors
  int maxResults,           // Max similarity search results
  Similarity similarity,    // Distance metric
  float threshold,          // Minimum similarity score
  IndexType indexType,      // Index structure
  boolean readOnly,         // Read-only mode
  boolean allowEviction,    // Enable eviction
  long evictTime,          // Eviction time
  TimeUnit evictTimeUnit   // Time unit for eviction
)
```

## Enums

### Similarity

Distance metrics with normalization:

- `EUCLIDEAN`: Euclidean distance with normalization formula `2 / (2 + max(0, distance))`
- `COSINE`: Cosine similarity
- `DOT`: Dot product similarity

Each provides a `normalizeDistance(float distance)` method to convert distances to [0, 1] scores.

### IndexType

Vector index structures:

- `FLAT`: Brute-force search (accurate but slow for large datasets)
- `IVF`: Inverted File Index (balanced speed/accuracy)
- `HNSW`: Hierarchical Navigable Small World (fast approximate search)

## Implementing a Vector Store

1. **Create a new module** for your implementation
2. **Add dependency** on this API module:

```xml
<dependency>
  <groupId>io.gravitee.resource.ai.vector.store</groupId>
  <artifactId>gravitee-resource-ai-vector-store-api</artifactId>
  <version>1.0.0</version>
</dependency>
```

3. **Extend AiVectorStoreResource**:

```java
public class MyVectorStoreResource
  extends AiVectorStoreResource<MyVectorStoreConfiguration> {

  @Override
  public Completable add(VectorEntity vectorEntity) {
    // Implement adding vector to your store
  }

  @Override
  public Flowable<VectorResult> findRelevant(VectorEntity vectorEntity) {
    // Implement similarity search
  }

  @Override
  public void remove(VectorEntity vectorEntity) {
    // Implement vector removal
  }
}
```

4. **Create configuration class**:

```java
public class MyVectorStoreConfiguration implements ResourceConfiguration {
  private AiVectorStoreProperties properties;
  // Additional config fields
}
```
