👁️ ##SauronVisionCenter - Sistema de Prescrição Óptica Inteligente

Um aplicativo Android desenvolvido em Kotlin para gestão completa de 

receitas oftalmológicas, com geração automática de PDFs e armazenamento local seguro.

(Inspirado no "Olho que Tudo Vê" - mas sem poderes malignos!)
-------------------------------------------------------------------------------------------------------------------------------------------------------------
✨ Funcionalidades Principais

🔍 Cadastro Avançado de Receitas

    Formulário dinâmico com Jetpack Compose

    Dados binoculares (OD/OE) com campos para:

        ESF (Esfera)

        CIL (Cilindro)

        EIXO

        AV (Acuidade Visual)

    Seções para tipo de lente, cor e observações
-------------------------------------------------------------------------------------------------------------------------------------------------------------
📄 Gerador de PDF Profissional

    Layout automático com iTextPDF

    Cabeçalho personalizado com logo

    Dados formatados em tabelas clínicas

    Salvamento em /Downloads/ com permissão WRITE_EXTERNAL_STORAGE
-------------------------------------------------------------------------------------------------------------------------------------------------------------
💾 Banco de Dados Local (Room)

    Armazena histórico de prescrições

    Entidade principal:
    kotlin

    @Entity(tableName = "prescriptions")
    data class PrescriptionEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val patientName: String,
        val lastCheckup: String,
        // ... (todos os campos ópticos)
    )
-------------------------------------------------------------------------------------------------------------------------------------------------------------
🛠️ Tecnologias Utilizadas

Componente	    Detalhes

Linguagem	      Kotlin 1.9

UI	            Jetpack Compose (100% declarativo)

Arquitetura	    MVVM com Clean Architecture

Persistência	  Room Database + SQLite

PDF	iTextPDF    7.2.5

Injeção	        Hilt (opcional - se implementado)

-------------------------------------------------------------------------------------------------------------------------------------------------------------
📂 Estrutura do Projeto

SauronVisionCenter/

├── app/

│   ├── src/main/

│   │   ├── java/com/example/sauronvisioncenter/

|   |   |   |__ app/            # SauronVisionApplication

│   │   │   ├── database/       # Room (Dao, Entities)

│   │   │   ├── prescription/   # ViewModel, Form UI

|   |   |   |__ components/     # DatePicker, RadioOption (opções de uso)

│   │   │   ├── util/           # PdfGenerator

|   |   |   |__ model/          # Campos para banco de dados e formulário

│   │   │   └── di/             # Hilt Modules (se aplicável)

|   |   |   |__ raíz/           # MainActivity

│   │   └── res/                # Drawables, Strings...

├── build.gradle                # Dependências principais

-------------------------------------------------------------------------------------------------------------------------------------------------------------
🔧 Dependências Críticas

// Room
implementation "androidx.room:room-runtime:2.6.1"
kapt "androidx.room:room-compiler:2.6.1"

// iTextPDF
implementation "com.itextpdf:itext7-core:7.2.5"

// Compose
implementation "androidx.compose.material3:material3:1.2.0"
implementation "androidx.activity:activity-compose:1.8.2"
-------------------------------------------------------------------------------------------------------------------------------------------------------------
🎨 UI/UX

    Scaffold com scroll vertical

    DatePicker integrado para seleção de data

    Botões de ação fixos no rodapé:

        "Salvar e Gerar PDF" (verde)

        "Limpar Formulário" (vermelho com ícone de lixeira)

https://i.imgur.com/not_sauron.png (Mockup ilustrativo)
⚡ Como Executar

    Clone o repositório

    Adicione no local.properties:
    properties

PDF_STORAGE_PATH=/storage/emulated/0/Download/
OBS: também é possível enviar o PDF via bluetooth, impressora conectada, computador ou mesmo Google Drive.

Build:
bash

    ./gradlew assembleDebug
-----------------------------------------------------------------------------
🔮 Roadmap Futuro

Feature	                      Status

Assinatura digital	         🚀 Previsto

Sync com nuvem (Firestore)	💡 Ideia

Histórico de pacientes	    🔄 Em dev
-----------------------------------------------------------------------------
📜 Licença

Projeto open-source sob licença MIT.

Desenvolvido com DeepSeek e Claude Chat e ☕ em Rondonópolis/MT!
Projeto open-source sob licença MIT.

Desenvolvido com DeepSeek Chat e ☕ em Rondonópolis/MT!
