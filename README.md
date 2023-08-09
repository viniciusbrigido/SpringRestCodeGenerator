# Plugin Gerador de API Java Spring - CodeGenerator
Este plugin é um gerador de código Java Spring que permite criar APIs REST de maneira rápida e eficiente. Ele gera automaticamente as entidades, repositórios, serviços, implementações de serviço e controladores com endpoints completos de CRUDL (Create, Read, Update, Delete e List).

### Requisitos

- Java 8+
- Spring 3+ previamente configurado (`spring-boot-starter`, `spring-boot-starter-data-jdbc`, `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, ...)
- biblioteca ModelMapper (`org.modelmapper`) no pom do projeto alvo


```java
@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
```

## A definição de entidades segue a seguinte sintaxe de objetos:

#### Envio completo:


```java
public class RequestDTO {
  private List<TableDTO> tables;
  private List<EnumDTO> enums;
}
```

```json
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
```

### Sintaxe de definição de entidades

```java
public class TableDTO {
    private String table;
    private List<ColumnDTO> columns;
}
```

```java
public class ColumnDTO {
    private String name;
    private String type;
    private Integer length;
    private Boolean primaryKey;
    private Boolean required;
    private Boolean list;
    private Boolean unique;
    private Boolean updatable;
    private String generationType;
    private String cardinality;
    private String enumType;
    private String mappedBy;
}
```

`table:` Nome da entidade (deve ser camelCase e iniciar com letra Maiúscula).

`name:` Nome da coluna (deve ser camelCase). No banco de dados, será convertido para snake_case, seguindo as boas práticas de nomenclatura de banco.

`type:` Tipo da coluna (deve ser o exato nome da propriedade/objeto, como String, Integer, Venda, etc.).

`length (opcional, default 255):` Indica comprimento da coluna (Somente se a coluna for do tipo String).

`primaryKey (opcional, default false):` Indica que a coluna será a chave primária da entidade.

`required (opcional, default false):` Indica que a coluna é obrigatória ao cadastrar. Será adicionada a anotação `@NotNull` no DTO de persistência para garantir que o usuário informe o valor.

`list (opcional, default false):` Indica que a coluna será uma lista.

`unique (opcional, default false):` Indica que a coluna terá a Constraint unique.

`updatable (opcional, default true):` Indica que a coluna pode ser atualizada. Será criado um DTO específico para atualização. Caso nenhuma propriedade possa ser atualizada, o DTO e o método de atualização correspondente não serão gerados.

`cardinality (opcional, case insensitive):` Indica o tipo de cardinalidade da coluna. Pode ser um dos seguintes valores: `OneToMany`, `ManyToOne`, `OneToOne` ou `ManyToMany`.

`generationType (opcional, case insensitive):` Indica o tipo de geração da coluna que contém a anotação `@GeneratedValue`. Pode ser um dos seguintes valores: `Table`, `Sequence`, `Identity`, `UUID` ou `Auto`.

`enumType (opcional, case insensitive, default STRING):` Indica a forma que a coluna de enum será persistida. Pode ser um dos seguintes valores: `String` ou `Ordinal`.

`mappedBy (opcional, case sensitive):` Usado em relacionamentos bidirecionais no JPA. Serve para indicar que o controle do relacionamento está no lado oposto da associação.

#### Aqui está um exemplo de definição de entidade:

```json
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
```

### Sintaxe de definição de enums

A definição de enums segue a seguinte sintaxe:

```java
public class EnumDTO {
    private String name;
    private List<String> values;
}
```

`name:` Nome do enum (deve ser camelCase).

`values:` Valores do enum (serão convertidos para snake_case e em letras maiúsculas para atender aos padrões de enum).

#### Aqui está um exemplo de definição de enum:

```json
{
  "name": "Raca",
  "values": ["golden", "viraLata"]
}
```

### Como utilizar
Para utilizar o gerador, siga as etapas abaixo:

1- Instale o Plugin

2- Na IDE, aperte o atalho `Ctrl + Alt + G`, abrirá a seguinte modal para configuração

![Modal](modal.jpg)


3 - Faça as personalizações adicionais necessárias para atender aos requisitos específicos do seu projeto e clique em `Gerar`.

4- Uma breve explicação dos campos:
- `Pasta Principal do Projeto:` Pasta onde os arquivos e pastas serão criados (Pasta em que contém o arquivo com o método `main`).
- `Arquivo de Geração:` Arquivo que contém as entidades/enums a serem gerados seguindo a nomenclatura citada (`JSON`).
- `Usar Lombok:` Determina se o projeto utiliza a biblioteca Lombok. Caso marcado, `getters`, `setters` e `construtores` das entidades e DTOs serão criados com `Lombok`.
- `Usar Serializable:` Determina se as entidades já iniciarão implementando a interface `Serializable`.
- `Package Entity (opcional):` Permite ao usuário identificar o pacote de entidades com um nome diferente de `entity` (por exemplo, `model`).
- `Package Controller (opcional):` Permite ao usuário identificar o pacote de controladores com um nome diferente de `controller` (por exemplo, `resource`).

### Observações

- Certifique-se de que todas as dependências do Java Spring necessárias para a execução do projeto sejam adicionadas corretamente.

- Após a geração do código, faça as devidas personalizações para atender aos requisitos específicos do seu projeto.

- É recomendável revisar e testar o código gerado para garantir que tudo esteja funcionando corretamente.

- Caso seja definido no JSON uma entidade que já existe no código ela, será apagada e gerada novamente. 