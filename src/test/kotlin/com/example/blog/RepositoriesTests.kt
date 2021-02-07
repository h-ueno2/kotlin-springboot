package com.example.blog

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class RepositoriesTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val articleRepository: ArticleRepository) {

    @Test
    fun `When findByIdOrNull then return Article`() {
        // ---テストデータの作成
        val juergen = User("springjuergen", "Juergen", "Hoeller")
        // Userをmanagerの管理下に置く。Insertみたいなイメージ（Commitは行わないが。）
        entityManager.persist(juergen)

        val article = Article("Spring Framework 5.0 goes GA",
                "Dear Spring community ...",
                "Lorem ipsum",
                juergen)
        entityManager.persist(article)

        // 管理下においていたEntityをcommitするイメージ
        entityManager.flush()

        // ---テストの実施
        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    fun `When findByLogin then return  User`() {
        val juergen = User("springjuergen", "Juergen", "Hoeller")
        entityManager.persist(juergen)
        entityManager.flush()
        val user = userRepository.findByLogin(juergen.login)
        assertThat(user).isEqualTo(juergen)
    }

}