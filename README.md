# 💵 moneyFlow - Seu app de gestão financeira

## 🔧 Como contribuir

Para manter o projeto organizado e evitar conflitos, siga o fluxo abaixo:

### 1. 📥 Clonar o repositório

```bash
git clone https://github.com/seu-usuario/moneyFlow.git
cd moneyFlow
```

---

### 2. 🌿 Criar uma branch

Nunca trabalhe direto na `main`.

```bash
git checkout -b feature/nome-da-feature
```

Exemplos:

* `feature/login`
* `feature/tela-home`
* `fix/bug-transacao`

---

### 3. 💻 Desenvolver a funcionalidade

* Siga a estrutura de pastas do projeto
* Separe responsabilidades (DAO, Service, Controller, View)
* Evite colocar lógica de negócio na interface (Swing)

---

### 4. ✅ Testar antes de subir

Antes de fazer commit:

* Teste a funcionalidade manualmente
* Verifique se não quebrou outras partes do sistema
* Confirme se o banco SQLite está funcionando corretamente

---

### 5. 💾 Commitar mudanças

Use mensagens claras:

```bash
git commit -m "feat: adiciona cadastro de transações"
```

Padrão sugerido:

* `feat:` nova funcionalidade
* `fix:` correção de bug
* `refactor:` melhoria de código
* `docs:` documentação

---

### 6. 🚀 Enviar para o repositório

```bash
git push origin feature/nome-da-feature
```

---

### 7. 🔀 Criar Pull Request

* Explique o que foi feito
* Adicione prints (se for interface)
* Aguarde revisão antes de merge

---

## 📋 Padrões do Projeto

### 🧱 Arquitetura

* Seguir padrão em camadas:

  * `model` → dados
  * `dao` → acesso ao banco
  * `service` → regras de negócio
  * `controller` → lógica da aplicação
  * `view` → interface gráfica

---

### 🎨 Interface (Swing)

* Evitar lógica dentro das Views
* Criar componentes reutilizáveis (`components/`)
* Usar dialogs para formulários

---

### 🗄️ Banco de Dados (SQLite)

* Não alterar estrutura do banco sem avisar o grupo
* Centralizar conexão em `DatabaseConfig.java`

---

### 🔐 Segurança

* Senhas devem ser armazenadas com hash usando Password4j
* Nunca salvar senha em texto puro

---

## ⚠️ Boas práticas

* Código limpo e organizado
* Nomes claros (em português ou inglês, mas consistente)
* Evitar duplicação de código
* Comentar apenas quando necessário
* Manter padrão entre todos os arquivos

---

## 🧪 Sugestões de melhorias futuras

* Sistema de autenticação completo
* Dashboard com gráficos (JFreeChart)
* Exportação de relatórios
* Sistema de metas financeiras

---

## 💬 Comunicação

* Alinhar tarefas antes de começar
* Evitar duas pessoas mexendo na mesma parte
* Avisar mudanças importantes

---

## 🔥 Observação importante

Seguir o fluxo de branch e padrôes de commit

---

Se quiser, posso te ajudar a deixar o README **nível portfólio (bem chamativo mesmo)** com:

* badges
* imagens da interface
* roadmap visual
* descrição mais forte (tipo produto real)

Só falar 👍
