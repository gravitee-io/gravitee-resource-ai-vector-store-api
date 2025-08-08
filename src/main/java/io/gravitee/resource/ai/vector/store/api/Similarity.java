/*
 * Copyright © 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.resource.ai.vector.store.api;

/**
 * @author Rémi SULTAN (remi.sultan at graviteesource.com)
 * @author GraviteeSource Team
 */
public enum Similarity {
  EUCLIDEAN, COSINE, DOT;

  public float normalizeDistance(float distance) {
    if (Float.isNaN(distance) || Float.isInfinite(distance)) return 0f;
    return switch (this) {
      case EUCLIDEAN -> 2f / (2f + Math.max(0f, distance));
      case COSINE, DOT -> (2f - (distance < 0f ? 0f : (Math.min(distance, 2f)))) / 2f;
    };
  }
}
