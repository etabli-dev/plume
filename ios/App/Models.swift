// Copyright 2026 R. Heller
// SPDX-License-Identifier: Apache-2.0

import Foundation

// Decoded form of Resources/Dictionary.json. Loaded once at launch.
public struct TeXCommand: Codable, Identifiable, Hashable, Sendable {
    public var id: String { command }   // commands are unique within the JSON
    public let command: String
    public let name: String
    public let preview: String
    public let category: String
    public let `package`: String?
    public let usage: String?
}

public struct TeXSnippet: Codable, Identifiable, Hashable, Sendable {
    public var id: String { title }
    public let title: String
    public let category: String
    public let code: String
}

public struct TeXDictionary: Codable, Sendable {
    public let version: Int
    public let categories: [String]
    public let entries: [TeXCommand]
    public let snippets: [TeXSnippet]

    /// Loaded from the bundled JSON. If decoding fails we return an empty
    /// dictionary and surface the error in the UI — we don't crash.
    public static func loadFromBundle() -> Result<TeXDictionary, Error> {
        guard let url = Bundle.main.url(forResource: "Dictionary", withExtension: "json") else {
            return .failure(LoadError.missing)
        }
        do {
            let data = try Data(contentsOf: url)
            let decoded = try JSONDecoder().decode(TeXDictionary.self, from: data)
            return .success(decoded)
        } catch {
            return .failure(error)
        }
    }

    public enum LoadError: LocalizedError {
        case missing
        public var errorDescription: String? {
            "Dictionary.json missing from app bundle."
        }
    }
}
