# Plugin Gerador de API Java Spring - CodeGenerator
Este plugin é um gerador de código Java Spring que permite criar APIs REST de maneira rápida e eficiente. Ele gera automaticamente as entidades, repositórios, serviços, implementações de serviço e controladores com endpoints completos de CRUDL (Create, Read, Update, Delete e List).

### Requisitos

- Java 8+
- Spring 3+ previamente configurado (`spring-boot-starter`, `spring-boot-starter-data-jdbc`, `spring-boot-starter-data-jpa`, ...)
- biblioteca ModelMapper (`org.modelmapper`) no pom do projeto alvo
<pre>
<span style="color: rgba(255, 165, 0, 0.7);">import</span> org.springframework.context.annotation.<span style="color: yellow;">Configuration</span>;
<span style="color: rgba(255, 165, 0, 0.7);">import</span> org.springframework.context.annotation.<span style="color: yellow;">Bean</span>;
<span style="color: rgba(255, 165, 0, 0.7);">import</span> org.modelmapper.ModelMapper;

<span style="color: yellow;">@Configuration</span>
<span style="color: rgba(255, 165, 0, 0.7);">public class</span> BeanConfig {

    <span style="color: yellow;">@Bean</span>
    <span style="color: rgba(255, 165, 0, 0.7);">public</span> ModelMapper <span style="color: #c0c043;">modelMapper()</span> {
        ModelMapper modelMapper = <span style="color: rgba(255, 165, 0, 0.7);">new</span> ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(<span style="color: rgba(255, 165, 0, 0.7);">true</span>);
        <span style="color: rgba(255, 165, 0, 0.7);">return</span> modelMapper;
    }
}
</pre>


## A definição de entidades segue a seguinte sintaxe de objetos:

#### Envio completo:

<pre>
<span style="color: rgba(255, 165, 0, 0.7);">public class</span> RequestDTO {
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> List &lt TableDTO &gt <span style="color: rgba(166,127,210,0.7);">tables</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> List &lt EnumDTO &gt <span style="color: rgba(166,127,210,0.7);">enums</span>;
}
</pre>

<pre>
{
    "tables": [
        {
            "table": "Cachorro",
            "columns": [
                {
                    "name": "id",
                    "type": "UUID",
                    "primaryKey": true
                },
                {
                    "name": "raca",
                    "type": "Raca"
                }
            ]
        }
    ],
    "enums": [
	    {
	    	"name": "Raca",
	    	"values": ["golden", "viraLata"]
	    }
    ]
}
</pre>

### Sintaxe de definição de entidades

<pre>
<span style="color: rgba(255, 165, 0, 0.7);">public class</span> TableDTO {
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> String <span style="color: rgba(166,127,210,0.7);">table</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> List &lt ColumnDTO &gt <span style="color: rgba(166,127,210,0.7);">columns</span>;
}
</pre>

<pre>
<span style="color: rgba(255, 165, 0, 0.7);">public class</span> ColumnDTO {
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> String <span style="color: rgba(166,127,210,0.7);">name</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> String <span style="color: rgba(166,127,210,0.7);">type</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> Integer <span style="color: rgba(166,127,210,0.7);">length</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private boolean</span> <span style="color: rgba(166,127,210,0.7);">primaryKey</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private boolean</span> <span style="color: rgba(166,127,210,0.7);">required</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private boolean</span> <span style="color: rgba(166,127,210,0.7);">updatable</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private boolean</span> <span style="color: rgba(166,127,210,0.7);">list</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> String <span style="color: rgba(166,127,210,0.7);">generationType</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> String <span style="color: rgba(166,127,210,0.7);">cardinality</span>;
}
</pre>

`table:` Nome da entidade (deve ser camelCase e iniciar com letra Maiúscula).

`name:` Nome da coluna (deve ser camelCase). No banco de dados, será convertido para snake_case, seguindo as boas práticas de nomenclatura de banco.

`type:` Tipo da coluna (deve ser o exato nome da propriedade/objeto, como String, Integer, Venda, etc.).

`primaryKey (opcional):` Indica que a coluna será a chave primária da entidade.

`required (opcional):` Indica que a coluna é obrigatória ao cadastrar. Será adicionada a anotação `@NotNull` no DTO de persistência para garantir que o usuário informe o valor.

`list (opcional):` Indica que a coluna será uma lista.

`updatable (opcional):` Indica que a coluna pode ser atualizada. Será criado um DTO específico para atualização. Caso nenhuma propriedade possa ser atualizada, o DTO e o método de atualização correspondente não serão gerados.

`generationType (opcional, case insensitive):` Indica o tipo de geração da coluna que contém a anotação `@GeneratedValue`. Pode ser um dos seguintes valores: `OneToMany`, `ManyToOne`, `OneToOne`, `ManyToMany`.

`cardinality (opcional, case insensitive):` Indica o tipo de cardinalidade da coluna. Pode ser um dos seguintes valores: `Table`, `Sequence`, `Identity`, `UUID`, `Auto`.


#### Aqui está um exemplo de definição de entidade:

<pre>
{
      "table": "Cachorro",
      "columns": [
        {
          "name": "id",
          "type": "UUID",
          "primaryKey": true
        },
        {
          "name": "raca",
          "type": "Raca"
        }
      ]
    }
</pre>

### Sintaxe de definição de enums

A definição de enums segue a seguinte sintaxe:

<pre>
<span style="color: rgba(255, 165, 0, 0.7);">public class</span> EnumDTO {
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> String <span style="color: rgba(166,127,210,0.7);">name</span>;
    <span style="color: rgba(255, 165, 0, 0.7);">private</span> List &lt String &gt <span style="color: rgba(166,127,210,0.7);">values</span>;
}
</pre>

`name:` Nome do enum (deve ser camelCase).

`values:` Valores do enum (serão convertidos para snake_case e em letras maiúsculas para atender aos padrões de enum).

#### Aqui está um exemplo de definição de enum:

<pre>
{
	"name": "Raca",
	"values": ["golden", "viraLata"]
}
</pre>

### Como utilizar
Para utilizar o gerador, siga as etapas abaixo:

1- Instale o Plugin

2- Na IDE, aperte o atalho `Ctrl + G`, abrirá a seguinte modal para configuração

![Modal](modal.jpg)


3 - Faça as personalizações adicionais necessárias para atender aos requisitos específicos do seu projeto e clique em `Gerar`.

4- Uma breve explicação dos campos:
- `Pasta Principal do Projeto:` Pasta onde os arquivos e pastas serão criados (Pasta em que contém o arquivo com o método `main`)
- `Arquivo de Geração:` Arquivo que contém as entidades/enums a serem gerados seguindo a nomenclatura citada (`JSON`)
- `Usar Lombok:` Determina se o projeto utiliza a biblioteca Lombok. Caso marcado, `getters`, `setters` e `construtores` das entidades e DTOs serão criados com `Lombok`
- `Usar Serializable:` Determina se as entidades já iniciarão implementando a interface `Serializable`
- `Package Entity (Opcional):` Permite ao usuário identificar o pacote de entidades com um nome diferente de `entity` (por exemplo, `model`).
- `Package Controller (Opcional):` Permite ao usuário identificar o pacote de controladores com um nome diferente de `controller` (por exemplo, `resource`).

### Observações

- Certifique-se de que todas as dependências do Java Spring necessárias para a execução do projeto sejam adicionadas corretamente.

- Após a geração do código, faça as devidas personalizações para atender aos requisitos específicos do seu projeto.

- É recomendável revisar e testar o código gerado para garantir que tudo esteja funcionando corretamente.
