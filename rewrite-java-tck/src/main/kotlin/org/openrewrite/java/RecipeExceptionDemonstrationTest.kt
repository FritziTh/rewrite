/*
 * Copyright 2022 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openrewrite.InMemoryExecutionContext
import org.openrewrite.java.Assertions.java
import org.openrewrite.test.RewriteTest
import org.openrewrite.test.SourceSpecs.text

interface RecipeExceptionDemonstrationTest : RewriteTest {

    @BeforeEach
    fun beforeEach() {
        RecipeExceptionDemonstration.DemonstrationException.restrictStackTrace = true
    }

    @AfterEach
    fun afterEach() {
        RecipeExceptionDemonstration.DemonstrationException.restrictStackTrace = false
    }

    @Test
    fun getVisitorOnMatchingMethod() = rewriteRun(
            { spec ->
                spec
                        .recipe(RecipeExceptionDemonstration("java.util.List add(..)", null, null,
                                null, null, null, null))
                        .executionContext(InMemoryExecutionContext())
            },
            java(
                    """
                import java.util.*;
                class Test {
                    void test(List<Integer> list) {
                        list.add(42);
                    }
                }
            """,
                    """
                import java.util.*;
                class Test {
                    void test(List<Integer> list) {
                        /*~~(org.openrewrite.java.RecipeExceptionDemonstration${'$'}DemonstrationException: Demonstrating an exception thrown on a matching method.
                  org.openrewrite.java.RecipeExceptionDemonstration${'$'}4.visitMethodInvocation(RecipeExceptionDemonstration.java:136)
                  org.openrewrite.java.RecipeExceptionDemonstration${'$'}4.visitMethodInvocation(RecipeExceptionDemonstration.java:130))~~>*/list.add(42);
                    }
                }
            """
            )
    )

    @Test
    fun applicableTest() = rewriteRun(
            { spec ->
                spec
                        .recipe(RecipeExceptionDemonstration(null, null, null,
                                true, null, null, null))
                        .executionContext(InMemoryExecutionContext())
            },
            java(
                    """
                    import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """
            ),
            text(
                    null,
                    "~~(org.openrewrite.java.RecipeExceptionDemonstration${'$'}DemonstrationException: Throwing on the project-level applicable test.)~~>" +
                            "Rewrite encountered an uncaught recipe error in org.openrewrite.java.RecipeExceptionDemonstration."
            ) { spec -> spec.path("recipe-exception-1.txt") }
    )

    @Test
    fun singleSourceApplicableTest() = rewriteRun(
            { spec ->
                spec
                        .recipe(RecipeExceptionDemonstration(null, null, null,
                                null, null, true, null))
                        .executionContext(InMemoryExecutionContext())
            },
            java(
                    """
                    import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """,
                    """
                    /*~~(org.openrewrite.java.RecipeExceptionDemonstration${'$'}DemonstrationException: Demonstrating an exception thrown on the single-source applicable test.)~~>*/import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """
            )
    )

    @Test
    fun applicableTestVisitor() = rewriteRun(
            { spec ->
                spec
                        .recipe(RecipeExceptionDemonstration(null, null, null,
                                null, true, null, null))
                        .executionContext(InMemoryExecutionContext())
            },
            java(
                    """
                    import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """,
                    """
                    /*~~(org.openrewrite.java.RecipeExceptionDemonstration${'$'}DemonstrationException: Throwing on the project-level applicable test.
                      org.openrewrite.java.RecipeExceptionDemonstration${'$'}1.preVisit(RecipeExceptionDemonstration.java:75)
                      org.openrewrite.java.RecipeExceptionDemonstration${'$'}1.preVisit(RecipeExceptionDemonstration.java:72))~~>*/import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """
            )
    )

    @Test
    fun visitAllVisitor() = rewriteRun(
            { spec ->
                spec
                        .recipe(RecipeExceptionDemonstration(null, null, true,
                                null, null, null, null))
                        .executionContext(InMemoryExecutionContext())
            },
            java(
                    """
                    import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """,
                    """
                    /*~~(org.openrewrite.java.RecipeExceptionDemonstration${'$'}DemonstrationException: Demonstrating an exception thrown in the recipe's `visit(List<SourceFile>, ExecutionContext)` method.
                      org.openrewrite.java.RecipeExceptionDemonstration${'$'}3.preVisit(RecipeExceptionDemonstration.java:117)
                      org.openrewrite.java.RecipeExceptionDemonstration${'$'}3.preVisit(RecipeExceptionDemonstration.java:114))~~>*/import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """
            )
    )

    @Test
    fun visitAll() = rewriteRun(
            { spec ->
                spec
                        .recipe(RecipeExceptionDemonstration(null, true, null,
                                null, null, null, null))
                        .executionContext(InMemoryExecutionContext())
            },
            java(
                    """
                    import java.util.*;
                    class Test {
                        void test(List<Integer> list) {
                            list.add(42);
                        }
                    }
                """
            ),
            text(
                    null,
                    "~~(org.openrewrite.java.RecipeExceptionDemonstration${'$'}DemonstrationException: Demonstrating an exception thrown in the recipe's `visit(List<SourceFile>, ExecutionContext)` method.)~~>" +
                            "Rewrite encountered an uncaught recipe error in org.openrewrite.java.RecipeExceptionDemonstration."
            ) { spec -> spec.path("recipe-exception-1.txt") }
    )
}