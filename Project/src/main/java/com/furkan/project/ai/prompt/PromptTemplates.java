package com.furkan.project.ai.prompt;

import java.math.BigDecimal;

public final class PromptTemplates {
    private PromptTemplates() {}

    public static String movieSystemPrompt() {
        return """
            You are a helpful movie assistant.

            HARD RULES:
            - Output in Turkish.
            - Exactly ONE short paragraph (max 80 words).
            - NO greetings, NO questions, NO placeholders like [Film Adı] or [Yönetmen Adı].
            - If title is provided, MENTION the title once (as-is).
            - No spoilers. Be concise and friendly.
            - If information is missing, just omit it; do not invent details.
            """;
    }

    public static String userPrompt(String username,
                                    String title,
                                    String release,
                                    String genres,
                                    String director,
                                    BigDecimal rating,
                                    String description,
                                    String tone) {
        return String.format("""
            Kullanıcı: %s
            Ton: %s
            Film adı: %s
            Vizyon tarihi: %s
            Türler: %s
            Yönetmen: %s
            Puan: %s
            Kısa özet: %s

            Talimat:
            - Yalnızca bir paragraf kısa YORUM yaz.
            - Selamlama cümleleri (“Merhaba”, “Tabii ki” vb.) YASAK.
            - Soru sorma.
            - Film adı verildiyse mutlaka geçir.
            """,
                username, (tone == null ? "neutral" : tone),
                title == null ? "" : title,
                release == null ? "" : release,
                genres == null ? "" : genres,
                director == null ? "" : director,
                rating == null ? "" : String.valueOf(rating),
                description == null ? "" : description
        );
    }

    public static String strictFixup(String title) {
        return String.format("""
            Önceki çıktı kurallara uymadı.
            Şimdi kurallara TAM uy ve sadece YORUM paragrafını döndür.
            Selamlama ve soru YOK. Placeholder YOK. Eğer film adı varsa kullan: %s
            """, title == null ? "" : title);
    }
}
